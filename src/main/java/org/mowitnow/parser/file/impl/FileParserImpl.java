package org.mowitnow.parser.file.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.mowitnow.beans.Area;
import org.mowitnow.beans.Orientation;
import org.mowitnow.beans.Position;
import org.mowitnow.beans.RotationAction;
import org.mowitnow.config.MowerConfig;
import org.mowitnow.parser.IParser;

public class FileParserImpl implements IParser {
	private static final String ERROR_PARSING_ORIENTATION = "Error parsing Orientation '{0}'";
	private static final String ERROR_PARSING_ROTATION_ACTION = "Error parsing Rotation action '{0}'";
	private static final String ERROR_PARSING_ACTIONS = "Parsing error can t read Actions";
	
	private BufferedReader mReader;
	private List<String> mLines;
	
	public FileParserImpl(String pFile) throws Exception {
		this(new File(pFile));
	}
	
	public FileParserImpl(File pFile) throws Exception {
		this(new FileInputStream(pFile));
	}
	
	public FileParserImpl(InputStream pStream) throws Exception {
		setStream(pStream);
		prepareLines();
	}
	
	private void prepareLines() throws Exception {
		mLines = new ArrayList<>();
		try{
			boolean lEndOfFile = false;
			do{
				String lLine = mReader.readLine();
				if(lLine == null){
					lEndOfFile = true;
				}else{
					mLines.add(lLine);
				}
			}while(!lEndOfFile);
		}finally {
			if(mReader != null){
				mReader.close();
			}
		}
	}

	@Override
	public List<MowerConfig> buildConfigs() throws Exception {
		List<MowerConfig> lConfigs = new ArrayList<>();
		boolean lEndOfConfs = false;
		MowerConfig lConfig = new MowerConfig();
		//read Area
		readArea(lConfig);
		Area lArea = lConfig.getArea();
		// read the rest
		do{
			readPosition(lConfig);
			Position lPosition = lConfig.getPosition();
			if(lPosition != null){
				readActions(lConfig);
				lConfigs.add(lConfig);
				//create a new config with the same area
				
				lConfig = new MowerConfig();
				lConfig.setArea(lArea);
			}else{
				lEndOfConfs = true;
			}
		}while(!lEndOfConfs);
		return lConfigs;
	}
	
	@Override
	public void readArea(MowerConfig pMowerCongif) throws Exception {
		if(mLines.size() > 0){
			String lAreaLine = mLines.remove(0);
			lAreaLine.trim();
			if(!StringUtils.isEmpty(lAreaLine)){
				Scanner lScanner = null;
				try{
					lScanner = new Scanner(lAreaLine);
					int lRows = lScanner.nextInt();
					int lCols = lScanner.nextInt();
					Area lArea = new Area(lRows, lCols);
					pMowerCongif.setArea(lArea);
				}finally{
					if(lScanner != null){
						lScanner.close();
					}
				}
			}
		}
	}

	@Override
	public void readPosition(MowerConfig pMowerCongif) throws Exception {
		if(mLines.size() > 0){
			String lPositionLine = mLines.remove(0);
			lPositionLine.trim();
			if(!StringUtils.isEmpty(lPositionLine)){
				Scanner lScanner = null;
				try{
					lScanner = new Scanner(lPositionLine);
					int lX = lScanner.nextInt();
					int lY = lScanner.nextInt();
					Character lOrientationCode = lScanner.next().charAt(0);
					Position lPosition = new Position(lX, lY);
					Orientation lOrientation = Arrays.asList(Orientation.values())
							.stream()
							.filter(lOrient -> lOrient.getCode().equals(lOrientationCode))
							.findAny()
							.orElse(null);
					if(lOrientation == null){
						throw new Exception(MessageFormat.format(ERROR_PARSING_ORIENTATION, lOrientationCode));
					}
					pMowerCongif.setPosition(lPosition);
					pMowerCongif.setOrientation(lOrientation);
				}finally{
					if(lScanner != null){
						lScanner.close();
					}
				}
			}
		}
	}

	@Override
	public void readActions(MowerConfig pMowerCongif) throws Exception {
		if(mLines.size() > 0){
			String lActionsLine = mLines.remove(0);
			lActionsLine.trim();
			// map all the rotation by code, easy to find by code
			Map<String, RotationAction> lRotations = Arrays.asList(RotationAction.values())
																.stream()
																.collect(Collectors.toMap(RotationAction::toString, lRotationAction -> lRotationAction));
			List<RotationAction> lRotationsActions = new ArrayList<>();
			for(Character lChar : lActionsLine.toCharArray()){
				RotationAction lRotationAction = lRotations.get(lChar.toString());
				lRotationsActions.add(lRotationAction);
				if(lRotationAction == null){
					throw new Exception(MessageFormat.format(ERROR_PARSING_ROTATION_ACTION, lChar));
				}
			}
			pMowerCongif.setRotationsActions(lRotationsActions);
		}else{
			throw new Exception(ERROR_PARSING_ACTIONS);
		}
	}

	@Override
	public void setStream(InputStream pStream) {
		mReader = new BufferedReader(new InputStreamReader(pStream));
	}
}
