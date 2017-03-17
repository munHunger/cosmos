package se.munhunger.folderscraper.utils.business;

import com.mscharhag.oleaster.runner.OleasterRunner;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import se.munhunger.folderscraper.utils.model.FileObject;

import java.io.File;

import static com.mscharhag.oleaster.matcher.Matchers.expect;
import static com.mscharhag.oleaster.runner.StaticRunnerSupport.*;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * Testing the business logic of the folder scraper
 * Created by marcus on 2017-03-17.
 */
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(OleasterRunner.class)
@PrepareForTest({File.class, Scraper.class})
public class ScraperTest
{
	{
		describe("Scraper", () ->
		{
			Scraper underTest = new Scraper();
			describe("Checking if file is downloaded", () ->
			{
				File fileMock = mock(File.class);
				beforeEach(() ->
				{
					mockStatic(File.class);
					PowerMockito.whenNew(File.class).withAnyArguments().thenReturn(fileMock);
				});
				describe("Download is a single file", () ->
				{
					beforeEach(() ->
							when(fileMock.isDirectory()).thenReturn(false));
					describe("Download is complete", () ->
					{
						beforeEach(() ->
								when(fileMock.exists()).thenReturn(false));
						it("Returns true", () ->
								expect(underTest.isDone(new FileObject(""))).toBeTrue());
					});
					describe("Download is incomplete", () ->
					{
						beforeEach(() ->
								when(fileMock.exists()).thenReturn(true));
						it("Returns true", () ->
								expect(underTest.isDone(new FileObject(""))).toBeFalse());
					});
				});
				describe("Download is a folder", () ->
				{
					beforeEach(() ->
							when(fileMock.isDirectory()).thenReturn(true));
					describe("Folder is empty", () ->
					{
						beforeEach(() ->
								when(fileMock.listFiles()).thenReturn(new File[0]));
						it("Returns true", () ->
								expect(underTest.isDone(new FileObject(""))).toBeTrue());
					});
					describe("Folder has content", () ->
					{
						beforeEach(() ->
								when(fileMock.listFiles()).thenReturn(new File[]{fileMock}));
						describe("Download is complete", () ->
						{
							beforeEach(() ->
									when(fileMock.getPath()).thenReturn("a.p"));
							it("Returns true", () ->
									expect(underTest.isDone(new FileObject(""))).toBeTrue());
						});
						describe("Download is incomplete", () ->
						{
							beforeEach(() ->
									when(fileMock.getPath()).thenReturn("a.part"));
							it("Returns true", () ->
									expect(underTest.isDone(new FileObject(""))).toBeFalse());
						});
					});
				});
			});
		});
	}
}