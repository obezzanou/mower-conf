package org.mowitnow.parser;

import java.io.InputStream;
import java.util.List;

import org.mowitnow.beans.Area;
import org.mowitnow.config.MowerConfig;

public interface IParser {
	public void readArea(MowerConfig pMowerCongif) throws Exception;
	public void readPosition(MowerConfig pMowerCongif) throws Exception;
	public void readActions(MowerConfig pMowerCongif) throws Exception;
	public void setStream(InputStream pStream);
	public List<MowerConfig> buildConfigs() throws Exception;
}
