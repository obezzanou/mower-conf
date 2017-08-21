package org.mowitnow.parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mowitnow.beans.Orientation;
import org.mowitnow.beans.RotationAction;
import org.mowitnow.config.MowerConfig;
import org.mowitnow.parser.file.impl.FileParserImpl;

public class ParserTest {

	private InputStream mTestStream;
	
	private IParser mParser;
	
	@Before
	public void beforTest() throws Exception {
		ByteArrayOutputStream lStream = new ByteArrayOutputStream();
		PrintWriter lWriter = new PrintWriter(new OutputStreamWriter(lStream));
		
		lWriter.println("5 5");
		lWriter.println("1 2 N");
		lWriter.println("GAGAGAGAA");
		lWriter.println("3 3 E");
		lWriter.println("AADAADADDA");
		
		lWriter.close();
		
		mTestStream = new ByteArrayInputStream(lStream.toByteArray());
		mParser = new FileParserImpl(mTestStream);
	}
	
	@Test
	public void testArea() throws Exception {
		List<MowerConfig> lConfigs = mParser.buildConfigs();
		
		assert lConfigs.size() == 2;
	}
	
	@Test
	public void testSizes() throws Exception {
		List<MowerConfig> lConfigs = mParser.buildConfigs();
		
		assert lConfigs.get(0).getArea().getRowSize() == 5 && lConfigs.get(0).getArea().getColSize() == 5;
	}
	
	@Test
	public void testPosition() throws Exception {
		List<MowerConfig> lConfigs = mParser.buildConfigs();
		
		assert lConfigs.get(0).getPosition().getX() == 1 && lConfigs.get(0).getPosition().getY() == 2;
	}
	
	@Test
	public void testOrientation() throws Exception {
		List<MowerConfig> lConfigs = mParser.buildConfigs();
		
		assert lConfigs.get(0).getOrientation() == Orientation.North;
	}
	
	@Test
	public void testRotationActions() throws Exception {
		List<MowerConfig> lConfigs = mParser.buildConfigs();
		
		assert lConfigs.get(0).getActions().size() == 9;
	}
	
	@Test
	public void testRotationAction() throws Exception {
		List<MowerConfig> lConfigs = mParser.buildConfigs();
		
		assert lConfigs.get(0).getActions().get(5) == RotationAction.A;
	}
}
