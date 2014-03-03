package eu.stratosphere.api.common.io;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

import eu.stratosphere.api.common.io.FileOutputFormat.OutputDirectoryMode;
import eu.stratosphere.configuration.Configuration;
import eu.stratosphere.core.fs.FileSystem.WriteMode;
import eu.stratosphere.core.fs.Path;
import eu.stratosphere.types.IntValue;
import eu.stratosphere.util.LogUtils;

public class FileOutputFormatTest {

	@BeforeClass
	public static void initialize() {
		LogUtils.initializeDefaultTestConsoleLogger();
	}
	
	@Test
	public void testCreateNoneParallelLocalFS() {
		
		File tmpOutPath = null;
		File tmpOutFile = null;
		try {
			tmpOutPath = File.createTempFile("fileOutputFormatTest", "Test1");
			tmpOutFile = new File(tmpOutPath.getAbsolutePath()+"/1");
		} catch (IOException e) {
			throw new RuntimeException("Test in error", e);
		}
		
		String tmpFilePath = tmpOutPath.toURI().toString();

		// check fail if file exists
		DummyFileOutputFormat dfof = new DummyFileOutputFormat();
		dfof.setOutputFilePath(new Path(tmpFilePath));
		dfof.setWriteMode(WriteMode.CREATE);
		dfof.setOutputDirectoryMode(OutputDirectoryMode.PARONLY);

		dfof.configure(new Configuration());
		
		boolean exception = false;
		try {
			dfof.open(0, 1);
			dfof.close();
		} catch (Exception e) {
			exception = true;
		}
		Assert.assertTrue(exception);

		// check fail if directory exists
		tmpOutPath.delete();
		Assert.assertTrue("Directory could not be created.", tmpOutPath.mkdir());

		dfof = new DummyFileOutputFormat();
		dfof.setOutputFilePath(new Path(tmpFilePath));
		dfof.setWriteMode(WriteMode.CREATE);
		dfof.setOutputDirectoryMode(OutputDirectoryMode.PARONLY);

		dfof.configure(new Configuration());
		
		exception = false;
		try {
			dfof.open(0, 1);
			dfof.close();
		} catch (Exception e) {
			exception = true;
		}
		Assert.assertTrue(exception);
		
		// check success
		tmpOutPath.delete();
		
		dfof = new DummyFileOutputFormat();
		dfof.setOutputFilePath(new Path(tmpFilePath));
		dfof.setWriteMode(WriteMode.CREATE);
		dfof.setOutputDirectoryMode(OutputDirectoryMode.PARONLY);

		dfof.configure(new Configuration());
		
		exception = false;
		try {
			dfof.open(0, 1);
			dfof.close();
		} catch (Exception e) {
			exception = true;
		}
		Assert.assertTrue(!exception);
		Assert.assertTrue(tmpOutPath.exists() && tmpOutPath.isFile());
		
		// ----------- test again with always directory mode
		
		// check fail if file exists
		dfof = new DummyFileOutputFormat();
		dfof.setOutputFilePath(new Path(tmpFilePath));
		dfof.setWriteMode(WriteMode.CREATE);
		dfof.setOutputDirectoryMode(OutputDirectoryMode.ALWAYS);

		dfof.configure(new Configuration());
		
		exception = false;
		try {
			dfof.open(0, 1);
			dfof.close();
		} catch (Exception e) {
			exception = true;
		}
		Assert.assertTrue(exception);

		// check success if directory exists
		tmpOutPath.delete();
		Assert.assertTrue("Directory could not be created.", tmpOutPath.mkdir());

		dfof = new DummyFileOutputFormat();
		dfof.setOutputFilePath(new Path(tmpFilePath));
		dfof.setWriteMode(WriteMode.CREATE);
		dfof.setOutputDirectoryMode(OutputDirectoryMode.ALWAYS);

		dfof.configure(new Configuration());
		
		exception = false;
		try {
			dfof.open(0, 1);
			dfof.close();
		} catch (Exception e) {
			exception = true;
		}
		Assert.assertTrue(!exception);
		Assert.assertTrue(tmpOutPath.exists() && tmpOutPath.isDirectory());
		Assert.assertTrue(tmpOutFile.exists() && tmpOutFile.isFile());
		
		// check fail if file in directory exists
		dfof = new DummyFileOutputFormat();
		dfof.setOutputFilePath(new Path(tmpFilePath));
		dfof.setWriteMode(WriteMode.CREATE);
		dfof.setOutputDirectoryMode(OutputDirectoryMode.ALWAYS);

		dfof.configure(new Configuration());
		
		exception = false;
		try {
			dfof.open(0, 1);
			dfof.close();
		} catch (Exception e) {
			exception = true;
		}
		Assert.assertTrue(exception);
		
		// check success if no file exists
		// delete existing files
		(new File(tmpOutPath.getAbsoluteFile()+"/1")).delete();
		tmpOutPath.delete();
		
		dfof = new DummyFileOutputFormat();
		dfof.setOutputFilePath(new Path(tmpFilePath));
		dfof.setWriteMode(WriteMode.CREATE);
		dfof.setOutputDirectoryMode(OutputDirectoryMode.ALWAYS);

		dfof.configure(new Configuration());
		
		exception = false;
		try {
			dfof.open(0, 1);
			dfof.close();
		} catch (Exception e) {
			exception = true;
		}
		Assert.assertTrue(!exception);
		Assert.assertTrue(tmpOutPath.exists() && tmpOutPath.isDirectory());
		Assert.assertTrue(tmpOutFile.exists() && tmpOutFile.isFile());
				
		// clean up
		(new File(tmpOutPath.getAbsoluteFile()+"/1")).delete();
		tmpOutPath.delete();
		
	}
	
	@Test
	public void testCreateParallelLocalFS() {
		
		File tmpOutPath = null;
		File tmpOutFile = null;
		try {
			tmpOutPath = File.createTempFile("fileOutputFormatTest", "Test1");
			tmpOutFile = new File(tmpOutPath.getAbsolutePath()+"/1");
		} catch (IOException e) {
			throw new RuntimeException("Test in error", e);
		}
		
		String tmpFilePath = tmpOutPath.toURI().toString();

		// check fail if file exists
		DummyFileOutputFormat dfof = new DummyFileOutputFormat();
		dfof.setOutputFilePath(new Path(tmpFilePath));
		dfof.setWriteMode(WriteMode.CREATE);
		dfof.setOutputDirectoryMode(OutputDirectoryMode.PARONLY);

		dfof.configure(new Configuration());
		
		boolean exception = false;
		try {
			dfof.open(0, 2);
			dfof.close();
		} catch (Exception e) {
			exception = true;
		}
		Assert.assertTrue(exception);

		// check success if directory exists
		tmpOutPath.delete();
		Assert.assertTrue("Directory could not be created.", tmpOutPath.mkdir());

		dfof = new DummyFileOutputFormat();
		dfof.setOutputFilePath(new Path(tmpFilePath));
		dfof.setWriteMode(WriteMode.CREATE);
		dfof.setOutputDirectoryMode(OutputDirectoryMode.PARONLY);

		dfof.configure(new Configuration());
		
		exception = false;
		try {
			dfof.open(0, 2);
			dfof.close();
		} catch (Exception e) {
			exception = true;
		}
		Assert.assertTrue(!exception);
		Assert.assertTrue(tmpOutPath.exists() && tmpOutPath.isDirectory());
		Assert.assertTrue(tmpOutFile.exists() && tmpOutFile.isFile());
		
		// check fail if file in directory exists
		dfof = new DummyFileOutputFormat();
		dfof.setOutputFilePath(new Path(tmpFilePath));
		dfof.setWriteMode(WriteMode.CREATE);
		dfof.setOutputDirectoryMode(OutputDirectoryMode.PARONLY);

		dfof.configure(new Configuration());
		
		exception = false;
		try {
			dfof.open(0, 2);
			dfof.close();
		} catch (Exception e) {
			exception = true;
		}
		Assert.assertTrue(exception);
		
		// check success if no file exists
		// delete existing files
		tmpOutFile.delete();
		tmpOutPath.delete();
		
		dfof = new DummyFileOutputFormat();
		dfof.setOutputFilePath(new Path(tmpFilePath));
		dfof.setWriteMode(WriteMode.CREATE);
		dfof.setOutputDirectoryMode(OutputDirectoryMode.PARONLY);

		dfof.configure(new Configuration());
		
		exception = false;
		try {
			dfof.open(0, 2);
			dfof.close();
		} catch (Exception e) {
			exception = true;
		}
		Assert.assertTrue(!exception);
		Assert.assertTrue(tmpOutPath.exists() && tmpOutPath.isDirectory());
		Assert.assertTrue(tmpOutFile.exists() && tmpOutFile.isFile());
		
		// clean up
		tmpOutFile.delete();
		tmpOutPath.delete();
	}
	
	@Test
	public void testOverwriteNoneParallelLocalFS() {
		
		File tmpOutPath = null;
		File tmpOutFile = null;
		try {
			tmpOutPath = File.createTempFile("fileOutputFormatTest", "Test1");
			tmpOutFile = new File(tmpOutPath.getAbsolutePath()+"/1");
		} catch (IOException e) {
			throw new RuntimeException("Test in error", e);
		}
		
		String tmpFilePath = tmpOutPath.toURI().toString();

		// check success if file exists
		DummyFileOutputFormat dfof = new DummyFileOutputFormat();
		dfof.setOutputFilePath(new Path(tmpFilePath));
		dfof.setWriteMode(WriteMode.OVERWRITE);
		dfof.setOutputDirectoryMode(OutputDirectoryMode.PARONLY);

		dfof.configure(new Configuration());
		
		boolean exception = false;
		try {
			dfof.open(0, 1);
			dfof.close();
		} catch (Exception e) {
			exception = true;
		}
		Assert.assertTrue(!exception);
		Assert.assertTrue(tmpOutPath.exists() && tmpOutPath.isFile());

		// check success if directory exists
		tmpOutPath.delete();
		Assert.assertTrue("Directory could not be created.", tmpOutPath.mkdir());

		dfof = new DummyFileOutputFormat();
		dfof.setOutputFilePath(new Path(tmpFilePath));
		dfof.setWriteMode(WriteMode.OVERWRITE);
		dfof.setOutputDirectoryMode(OutputDirectoryMode.PARONLY);

		dfof.configure(new Configuration());
		
		exception = false;
		try {
			dfof.open(0, 1);
			dfof.close();
		} catch (Exception e) {
			exception = true;
		}
		Assert.assertTrue(!exception);
		Assert.assertTrue(tmpOutPath.exists() && tmpOutPath.isFile());
		
		// check success
		tmpOutPath.delete();
		
		dfof = new DummyFileOutputFormat();
		dfof.setOutputFilePath(new Path(tmpFilePath));
		dfof.setWriteMode(WriteMode.OVERWRITE);
		dfof.setOutputDirectoryMode(OutputDirectoryMode.PARONLY);

		dfof.configure(new Configuration());
		
		exception = false;
		try {
			dfof.open(0, 1);
			dfof.close();
		} catch (Exception e) {
			exception = true;
		}
		Assert.assertTrue(!exception);
		Assert.assertTrue(tmpOutPath.exists() && tmpOutPath.isFile());
		
		// ----------- test again with always directory mode
		
		// check success if file exists
		dfof = new DummyFileOutputFormat();
		dfof.setOutputFilePath(new Path(tmpFilePath));
		dfof.setWriteMode(WriteMode.OVERWRITE);
		dfof.setOutputDirectoryMode(OutputDirectoryMode.ALWAYS);

		dfof.configure(new Configuration());
		
		exception = false;
		try {
			dfof.open(0, 1);
			dfof.close();
		} catch (Exception e) {
			exception = true;
		}
		Assert.assertTrue(!exception);
		Assert.assertTrue(tmpOutPath.exists() && tmpOutPath.isDirectory());
		Assert.assertTrue(tmpOutFile.exists() && tmpOutFile.isFile());

		// check success if directory exists
		tmpOutFile.delete();
		tmpOutPath.delete();
		Assert.assertTrue("Directory could not be created.", tmpOutPath.mkdir());

		dfof = new DummyFileOutputFormat();
		dfof.setOutputFilePath(new Path(tmpFilePath));
		dfof.setWriteMode(WriteMode.OVERWRITE);
		dfof.setOutputDirectoryMode(OutputDirectoryMode.ALWAYS);

		dfof.configure(new Configuration());
		
		exception = false;
		try {
			dfof.open(0, 1);
			dfof.close();
		} catch (Exception e) {
			exception = true;
		}
		Assert.assertTrue(!exception);
		Assert.assertTrue(tmpOutPath.exists() && tmpOutPath.isDirectory());
		Assert.assertTrue(tmpOutFile.exists() && tmpOutFile.isFile());
		
		// check success if file in directory exists
		dfof = new DummyFileOutputFormat();
		dfof.setOutputFilePath(new Path(tmpFilePath));
		dfof.setWriteMode(WriteMode.OVERWRITE);
		dfof.setOutputDirectoryMode(OutputDirectoryMode.ALWAYS);

		dfof.configure(new Configuration());
		
		exception = false;
		try {
			dfof.open(0, 1);
			dfof.close();
		} catch (Exception e) {
			exception = true;
		}
		Assert.assertTrue(!exception);
		Assert.assertTrue(tmpOutPath.exists() && tmpOutPath.isDirectory());
		Assert.assertTrue(tmpOutFile.exists() && tmpOutFile.isFile());
		
		// check success if no file exists
		// delete existing files
		tmpOutFile.delete();
		tmpOutPath.delete();
		
		dfof = new DummyFileOutputFormat();
		dfof.setOutputFilePath(new Path(tmpFilePath));
		dfof.setWriteMode(WriteMode.OVERWRITE);
		dfof.setOutputDirectoryMode(OutputDirectoryMode.ALWAYS);

		dfof.configure(new Configuration());
		
		exception = false;
		try {
			dfof.open(0, 1);
			dfof.close();
		} catch (Exception e) {
			exception = true;
		}
		Assert.assertTrue(!exception);
		Assert.assertTrue(tmpOutPath.exists() && tmpOutPath.isDirectory());
		Assert.assertTrue(tmpOutFile.exists() && tmpOutFile.isFile());
		
		// clean up
		tmpOutFile.delete();
		tmpOutPath.delete();
		
	}
	
	@Test
	public void testOverwriteParallelLocalFS() {
		
		File tmpOutPath = null;
		File tmpOutFile = null;
		try {
			tmpOutPath = File.createTempFile("fileOutputFormatTest", "Test1");
			tmpOutFile = new File(tmpOutPath.getAbsolutePath()+"/1");
		} catch (IOException e) {
			throw new RuntimeException("Test in error", e);
		}
		
		String tmpFilePath = tmpOutPath.toURI().toString();

		// check success if file exists
		DummyFileOutputFormat dfof = new DummyFileOutputFormat();
		dfof.setOutputFilePath(new Path(tmpFilePath));
		dfof.setWriteMode(WriteMode.OVERWRITE);
		dfof.setOutputDirectoryMode(OutputDirectoryMode.PARONLY);

		dfof.configure(new Configuration());
		
		boolean exception = false;
		try {
			dfof.open(0, 2);
			dfof.close();
		} catch (Exception e) {
			exception = true;
		}
		Assert.assertTrue(!exception);
		Assert.assertTrue(tmpOutPath.exists() && tmpOutPath.isDirectory());
		Assert.assertTrue(tmpOutFile.exists() && tmpOutFile.isFile());

		// check success if directory exists
		tmpOutFile.delete();
		tmpOutPath.delete();
		Assert.assertTrue("Directory could not be created.", tmpOutPath.mkdir());

		dfof = new DummyFileOutputFormat();
		dfof.setOutputFilePath(new Path(tmpFilePath));
		dfof.setWriteMode(WriteMode.OVERWRITE);
		dfof.setOutputDirectoryMode(OutputDirectoryMode.PARONLY);

		dfof.configure(new Configuration());
		
		exception = false;
		try {
			dfof.open(0, 2);
			dfof.close();
		} catch (Exception e) {
			exception = true;
		}
		Assert.assertTrue(!exception);
		Assert.assertTrue(tmpOutPath.exists() && tmpOutPath.isDirectory());
		Assert.assertTrue(tmpOutFile.exists() && tmpOutFile.isFile());
		
		// check success if file in directory exists
		dfof = new DummyFileOutputFormat();
		dfof.setOutputFilePath(new Path(tmpFilePath));
		dfof.setWriteMode(WriteMode.OVERWRITE);
		dfof.setOutputDirectoryMode(OutputDirectoryMode.PARONLY);

		dfof.configure(new Configuration());
		
		exception = false;
		try {
			dfof.open(0, 2);
			dfof.close();
		} catch (Exception e) {
			exception = true;
		}
		Assert.assertTrue(!exception);
		Assert.assertTrue(tmpOutPath.exists() && tmpOutPath.isDirectory());
		Assert.assertTrue(tmpOutFile.exists() && tmpOutFile.isFile());
		
		// check success if no file exists
		// delete existing files
		(new File(tmpOutPath.getAbsoluteFile()+"/1")).delete();
		tmpOutPath.delete();
		
		dfof = new DummyFileOutputFormat();
		dfof.setOutputFilePath(new Path(tmpFilePath));
		dfof.setWriteMode(WriteMode.OVERWRITE);
		dfof.setOutputDirectoryMode(OutputDirectoryMode.PARONLY);

		dfof.configure(new Configuration());
		
		exception = false;
		try {
			dfof.open(0, 2);
			dfof.close();
		} catch (Exception e) {
			exception = true;
		}
		Assert.assertTrue(!exception);
		Assert.assertTrue(tmpOutPath.exists() && tmpOutPath.isDirectory());
		Assert.assertTrue(tmpOutFile.exists() && tmpOutFile.isFile());
		
		// clean up
		tmpOutFile.delete();
		tmpOutPath.delete();
		
	}
	
	// -------------------------------------------------------------------------------------------
	
	public static class DummyFileOutputFormat extends FileOutputFormat<IntValue> {
		private static final long serialVersionUID = 1L;

		@Override
		public void writeRecord(IntValue record) throws IOException {
			// DO NOTHING
		}
	}
	
}
