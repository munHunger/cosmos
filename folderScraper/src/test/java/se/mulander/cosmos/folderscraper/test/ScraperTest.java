package se.mulander.cosmos.folderscraper.test;

import com.mscharhag.oleaster.matcher.matchers.ExceptionMatcher;
import com.mscharhag.oleaster.runner.OleasterRunner;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import se.mulander.cosmos.common.business.HttpRequest;
import se.mulander.cosmos.common.database.Database;
import se.mulander.cosmos.common.model.HttpResponse;
import se.mulander.cosmos.folderscraper.impl.Scraper;
import se.mulander.cosmos.folderscraper.model.FileObject;
import se.mulander.cosmos.folderscraper.model.OMDBResponse;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static com.mscharhag.oleaster.matcher.Matchers.expect;
import static com.mscharhag.oleaster.runner.StaticRunnerSupport.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * Testing the business logic of the folder scraper
 * Created by marcus on 2017-03-17.
 */
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(OleasterRunner.class)
@PrepareForTest({Scraper.class, HttpRequest.class})
public class ScraperTest
{

    {
        describe("Scraper", () -> {
            Scraper underTest = new Scraper();

            describe("Moving an object", () -> {
                FileObject object = new FileObject();
                describe("Object is not completed", () -> {
                    beforeEach(() -> object.isComplete = false);
                    it("throws IllegalArgumentException",
                       () -> new ExceptionMatcher(() -> underTest.moveObject(object)).toThrow(IllegalArgumentException.class));
                });
                describe("Object is completed", () -> {
                    beforeEach(() -> object.isComplete = true);
                    describe("Object is not a movie or a tv-show", () -> {
                        beforeEach(() -> {
                            object.isMovie = false;
                            object.isTV = false;
                        });
                        it("throws IllegalArgumentException",
                           () -> new ExceptionMatcher(() -> underTest.moveObject(object)).toThrow(
                                   IllegalArgumentException.class));
                    });
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
                    describe("Object is a TV-show", () -> {
                        beforeEach(() -> {
                            object.isTV = true;
                            object.isMovie = false;
                            mockStatic(Files.class);
                        });
                        it("Calls Files.move", () -> verifyStatic(times(1)));
                    });
                    describe("Object is a Movie", () -> {
                        beforeEach(() -> {
                            object.isMovie = true;
                            mockStatic(Files.class);
                        });
                        it("Calls Files.move", () -> verifyStatic(times(1)));
                    });
                });
            });
            describe("Checking folderstatus", () -> {
                File fileMock = mock(File.class);
                beforeEach(() -> PowerMockito.whenNew(File.class).withAnyArguments().thenReturn(fileMock));
                describe("Has no files or folders in watch path", () -> {
                    beforeEach(() -> when(fileMock.listFiles()).thenReturn(new File[0]));
                    it("returns an empty list", () -> expect(underTest.getFolderStatus().isEmpty()).toBeTrue());
                });
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
                describe("Has a completed file or folder", () -> {
                    Database dbMock = mock(Database.class);
                    Scraper spied = PowerMockito.spy(underTest);
                    beforeEach(() -> {
                        when(fileMock.listFiles()).thenReturn(new File[]{fileMock, fileMock});
                        when(fileMock.getPath()).thenReturn("abc");
                        PowerMockito.whenNew(Database.class).withAnyArguments().thenReturn(dbMock);
                        when(dbMock.saveObject(any())).thenReturn(true);
                        doReturn(null).when(spied).searchMetaData(any());
                    });
                    describe("Object is not in database", () -> {
                        beforeEach(() -> {
                            when(dbMock.getObject(any(Class.class), any(String.class))).thenReturn(new ArrayList<>());
                            when(dbMock.insertObject(any())).thenReturn(true);
                        });
                        it("adds it to the result list", () -> expect(spied.getFolderStatus().size()).toEqual(2));
                        it("calls the save operation of the database on all objects",
                           () -> verify(dbMock, times(2)).saveObject(any()));
                        it("calls the insert operation of the database on all objects",
                           () -> verify(dbMock, times(2)).insertObject(any()));
                        after(() -> Mockito.reset(dbMock));
                    });
                    describe("Object is already in the database", () -> {
                        beforeEach(() -> {
                            List<Object> returnList = new ArrayList<>();
                            returnList.add(mock(FileObject.class));
                            when(dbMock.getObject(any(Class.class), any(String.class))).thenReturn(returnList);
                        });
                        it("adds it to the result list", () -> expect(spied.getFolderStatus().size()).toEqual(2));
                        it("calls the save operation of the database on all objects",
                           () -> verify(dbMock, times(2)).saveObject(any()));
                        after(() -> Mockito.reset(dbMock));
                    });
                });
            });
            describe("Fetching metadata", () -> {
                FileObject toSearchFor = new FileObject("random.path.exe");
                beforeEach(() -> PowerMockito.whenNew(FileObject.class).withAnyArguments().thenReturn(toSearchFor));
                describe("Object cannot be found on omdbapi", () -> {
                    beforeEach(() -> {
                        OMDBResponse response = new OMDBResponse();
                        response.Response = "False";
                        mockStatic(HttpRequest.class);
                        when(HttpRequest.getRequest(any(String.class), any(Class.class))).thenReturn(new HttpResponse(
                                response,
                                200));
                    });
                    it("Doesn't change the object",
                       () -> expect(underTest.searchMetaData(toSearchFor)).toEqual(toSearchFor));
                });
                describe("Object can be found on omdbapi", () -> {
                    OMDBResponse response = new OMDBResponse();
                    beforeEach(() -> {
                        response.Response = "true";
                        mockStatic(HttpRequest.class);
                        when(HttpRequest.getRequest(any(String.class), any(Class.class))).thenReturn(new HttpResponse(
                                response,
                                200));
                    });
                    describe("Object is a tv-show", () -> {
                        beforeEach(() -> response.Type = "series");
                        it("Sets the tv-flag to true",
                           () -> expect(underTest.searchMetaData(toSearchFor).isTV).toBeTrue());
                        it("Sets the movie-flag to false",
                           () -> expect(underTest.searchMetaData(toSearchFor).isMovie).toBeFalse());
                    });
                });
            });
            describe("Checking if file is downloaded", () -> {
                File fileMock = mock(File.class);
                beforeEach(() -> PowerMockito.whenNew(File.class).withAnyArguments().thenReturn(fileMock));
                describe("Download is a single file", () -> {
                    beforeEach(() -> when(fileMock.isDirectory()).thenReturn(false));
                    describe("Download is complete", () -> {
                        beforeEach(() -> when(fileMock.exists()).thenReturn(false));
                        it("Returns true", () -> expect(underTest.isDone(new FileObject(""))).toBeTrue());
                    });
                    describe("Download is incomplete", () -> {
                        beforeEach(() -> when(fileMock.exists()).thenReturn(true));
                        it("Returns true", () -> expect(underTest.isDone(new FileObject(""))).toBeFalse());
                    });
                });
                describe("Download is a folder", () -> {
                    beforeEach(() -> when(fileMock.isDirectory()).thenReturn(true));
                    describe("Folder is empty", () -> {
                        beforeEach(() -> when(fileMock.listFiles()).thenReturn(new File[0]));
                        it("Returns true", () -> expect(underTest.isDone(new FileObject(""))).toBeTrue());
                    });
                    describe("Folder has content", () -> {
                        beforeEach(() -> when(fileMock.listFiles()).thenReturn(new File[]{fileMock}));
                        describe("Download is complete", () -> {
                            beforeEach(() -> when(fileMock.getPath()).thenReturn("a.p"));
                            it("Returns true", () -> expect(underTest.isDone(new FileObject(""))).toBeTrue());
                        });
                        describe("Download is incomplete", () -> {
                            beforeEach(() -> when(fileMock.getPath()).thenReturn("a.part"));
                            it("Returns true", () -> expect(underTest.isDone(new FileObject(""))).toBeFalse());
                        });
                    });
                });
            });
        });
    }
}