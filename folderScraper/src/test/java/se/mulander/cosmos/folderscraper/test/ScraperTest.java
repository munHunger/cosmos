package se.mulander.cosmos.folderscraper.test;

import com.mscharhag.oleaster.matcher.matchers.ExceptionMatcher;
import com.mscharhag.oleaster.runner.OleasterRunner;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import se.mulander.cosmos.common.database.jpa.Database;
import se.mulander.cosmos.common.model.movies.tmdb.TMDBResponse;
import se.mulander.cosmos.common.model.movies.tmdb.TMDBResponseResult;
import se.mulander.cosmos.folderscraper.impl.Scraper;
import se.mulander.cosmos.folderscraper.model.FileObject;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static com.mscharhag.oleaster.matcher.Matchers.expect;
import static com.mscharhag.oleaster.runner.StaticRunnerSupport.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * Testing the business logic of the folder scraper
 * Created by marcus on 2017-03-17.
 */
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(OleasterRunner.class)
@PrepareForTest({Scraper.class, Database.class, ClientBuilder.class})
public class ScraperTest
{
    {
        describe("Scraper", () -> {
            Scraper underTest = new Scraper();

            //region Moving an object
            describe("Moving an object", () -> {
                FileObject object = new FileObject();
                //region Object is not completed
                describe("Object is not completed", () -> {
                    beforeEach(() -> object.isComplete = false);
                    it("throws IllegalArgumentException",
                       () -> new ExceptionMatcher(() -> underTest.moveObject(object)).toThrow(IllegalArgumentException.class));
                });
                //endregion
                //region Object is completed
                describe("Object is completed", () -> {
                    beforeEach(() -> object.isComplete = true);
                    //region Object is not a movie or a tv-show
                    describe("Object is not a movie or a tv-show", () -> {
                        beforeEach(() -> {
                            object.isMovie = false;
                            object.isTV = false;
                        });
                        it("throws IllegalArgumentException",
                           () -> new ExceptionMatcher(() -> underTest.moveObject(object)).toThrow(
                                   IllegalArgumentException.class));
                    });
                    //endregion
                    //region Object is both a movie and a tv-show
                    describe("Object is both a movie and a tv-show", () -> {
                        beforeEach(() -> {
                            object.isTV = true;
                            object.isMovie = true;
                            mockStatic(Files.class);
                        });
                        it("throws IllegalArgumentException",
                           () -> new ExceptionMatcher(() -> underTest.moveObject(object)).toThrow(
                                   IllegalArgumentException.class));
                    });
                    //endregion
                    //region Object is a tv-show
                    describe("Object is a TV-show", () -> {
                        beforeEach(() -> {
                            object.isTV = true;
                            object.isMovie = false;
                            mockStatic(Files.class);
                        });
                        it("Calls Files.move", () -> verifyStatic(times(1)));
                    });
                    //endregion
                    //region Object is a movie
                    describe("Object is a Movie", () -> {
                        beforeEach(() -> {
                            object.isMovie = true;
                            mockStatic(Files.class);
                        });
                        it("Calls Files.move", () -> verifyStatic(times(1)));
                    });
                    //endregion
                });
                //endregion
            });
            //endregion
            //region Checking folderstatus
            describe("Checking folderstatus", () -> {
                File fileMock = mock(File.class);
                beforeEach(() -> PowerMockito.whenNew(File.class).withAnyArguments().thenReturn(fileMock));
                //region Has no files or folders in watch path
                describe("Has no files or folders in watch path", () -> {
                    beforeEach(() -> when(fileMock.listFiles()).thenReturn(new File[0]));
                    it("returns an empty list", () -> expect(underTest.getFolderStatus().isEmpty()).toBeTrue());
                });
                //endregion
                //region Has no completed files or folders in watch path
                describe("Has no completed files or folders in watch path", () -> {
                    beforeEach(() -> {
                        File partMock = mock(File.class);
                        File ignoreMock = mock(File.class);
                        when(fileMock.listFiles()).thenReturn(new File[]{partMock, ignoreMock});
                        when(ignoreMock.delete()).thenReturn(true);
                        when(partMock.getPath()).thenReturn("xxxpart");
                        when(ignoreMock.getPath()).thenReturn("xxxignore");
                    });
                    it("returns an empty list", () -> expect(underTest.getFolderStatus().isEmpty()).toBeTrue());
                });
                //endregion
                //region Has a completed file or folder
                describe("Has a completed file or folder", () -> {
                    Scraper spied = PowerMockito.spy(underTest);
                    beforeEach(() -> {
                        mockStatic(Database.class);
                        when(fileMock.listFiles()).thenReturn(new File[]{fileMock, fileMock});
                        when(fileMock.getPath()).thenReturn("abc");
                        doNothing().when(Database.class, "saveObject", any());
                        doNothing().when(Database.class, "updateObject", any());
                        doReturn(null).when(spied).searchMetaData(any());
                    });
                    //region Object is not in database
                    describe("Object is not in database", () -> {
                        beforeEach(() -> {
                            when(Database.getObjects(any(), any())).thenReturn(new ArrayList<>());
                            spied.getFolderStatus();
                        });
                        it("adds it to the result list", () -> expect(spied.getFolderStatus().size()).toEqual(2));
                        it("calls the update operation of the database on all objects", () -> {
                            verifyStatic(times(2));
                            Database.updateObject(any());
                        });
                        it("calls the save operation of the database on all objects", () -> {
                            verifyStatic(times(2));
                            Database.saveObject(any());
                        });
                    });
                    //endregion
                    //region Object is already in the database
                    describe("Object is already in the database", () -> {
                        beforeEach(() -> {
                            List<Object> returnList = new ArrayList<>();
                            returnList.add(mock(FileObject.class));
                            when(Database.getObjects(any(), any())).thenReturn(returnList);
                            spied.getFolderStatus();
                        });
                        it("adds it to the result list", () -> expect(spied.getFolderStatus().size()).toEqual(2));
                        it("calls the save operation of the database on all objects", () -> {
                            verifyStatic(times(2));
                            Database.updateObject(any());
                        });
                    });
                    //endregion
                });
                //endregion
            });
            //endregion
            //region Fetching metadata
            describe("Fetching metadata", () -> {
                FileObject toSearchFor = new FileObject("random.path.exe");
                beforeEach(() -> PowerMockito.whenNew(FileObject.class).withAnyArguments().thenReturn(toSearchFor));
                //region Object cannot be found on omdbapi
                describe("Object cannot be found on omdbapi", () -> {
                    beforeEach(() -> {
                        TMDBResponse tmdbResponse = new TMDBResponse();
                        tmdbResponse.results = new TMDBResponseResult[0];
                        mockResponse(TMDBResponse.class, tmdbResponse);
                    });
                    it("Doesn't change the object",
                       () -> expect(underTest.searchMetaData(toSearchFor)).toEqual(toSearchFor));
                });
                //endregion
                //region Object can be found on omdbapi
                describe("Object can be found on omdbapi", () -> {
                    TMDBResponse response = new TMDBResponse();
                    beforeEach(() -> {
                        response.results = new TMDBResponseResult[]{new TMDBResponse().buildResult("TV")};
                        mockResponse(TMDBResponse.class, response);
                    });
                    //region Object is a tv/show
                    describe("Object is a tv-show", () -> {
                        it("Sets the tv-flag to true",
                           () -> expect(underTest.searchMetaData(toSearchFor).isTV).toBeTrue());
                        it("Sets the movie-flag to false",
                           () -> expect(underTest.searchMetaData(toSearchFor).isMovie).toBeFalse());
                    });
                    //endregion
                });
                //endregion
            });
            //endregion
            //region Checking if file is downloaded
            describe("Checking if file is downloaded", () -> {
                File fileMock = mock(File.class);
                beforeEach(() -> PowerMockito.whenNew(File.class).withAnyArguments().thenReturn(fileMock));
                //region Download is a single file
                describe("Download is a single file", () -> {
                    beforeEach(() -> when(fileMock.isDirectory()).thenReturn(false));
                    //region Download is complete
                    describe("Download is complete", () -> {
                        beforeEach(() -> when(fileMock.exists()).thenReturn(false));
                        it("Returns true", () -> expect(underTest.isDone(new FileObject(""))).toBeTrue());
                    });
                    //endregion
                    //region Download is incomplete
                    describe("Download is incomplete", () -> {
                        beforeEach(() -> when(fileMock.exists()).thenReturn(true));
                        it("Returns true", () -> expect(underTest.isDone(new FileObject(""))).toBeFalse());
                    });
                    //endregion
                });
                //endregion
                //region Download is a folder
                describe("Download is a folder", () -> {
                    beforeEach(() -> when(fileMock.isDirectory()).thenReturn(true));
                    //region Folder is empty
                    describe("Folder is empty", () -> {
                        beforeEach(() -> when(fileMock.listFiles()).thenReturn(new File[0]));
                        it("Returns true", () -> expect(underTest.isDone(new FileObject(""))).toBeTrue());
                    });
                    //endregion
                    //region Folder has content
                    describe("Folder has content", () -> {
                        beforeEach(() -> when(fileMock.listFiles()).thenReturn(new File[]{fileMock}));
                        //region Download is complete
                        describe("Download is complete", () -> {
                            beforeEach(() -> when(fileMock.getPath()).thenReturn("a.p"));
                            it("Returns true", () -> expect(underTest.isDone(new FileObject(""))).toBeTrue());
                        });
                        //endregion
                        //region Download is incomplete
                        describe("Download is incomplete", () -> {
                            beforeEach(() -> when(fileMock.getPath()).thenReturn("a.part"));
                            it("Returns true", () -> expect(underTest.isDone(new FileObject(""))).toBeFalse());
                        });
                        //endregion
                    });
                    //endregion
                });
                //endregion
            });
            //endregion
        });
    }

    private static <T> void mockResponse(Class<T> type, T response) throws Exception
    {
        mockStatic(ClientBuilder.class);
        Client client = mock(Client.class);
        when(ClientBuilder.class, "newClient").thenReturn(client);
        WebTarget webTarget = mock(WebTarget.class);
        when(client.target(anyString())).thenReturn(webTarget);
        when(webTarget.path(anyString())).thenReturn(webTarget);
        when(webTarget.queryParam(any(), any())).thenReturn(webTarget);
        Invocation.Builder invocationBuilder = mock(Invocation.Builder.class);
        when(webTarget.request()).thenReturn(invocationBuilder);
        Invocation invocation = mock(Invocation.class);
        when(invocationBuilder.buildGet()).thenReturn(invocation);

        Response res = mock(Response.class);
        when(invocation.invoke()).thenReturn(res);
        when(res.readEntity(type)).thenReturn(response);
    }
}