package org.mowitnow.config;

import java.util.List;

import org.mowitnow.beans.Area;
import org.mowitnow.beans.Orientation;
import org.mowitnow.beans.Position;
import org.mowitnow.beans.RotationAction;

public class MowerConfig {
	private Area mArea;
	private Position mPosition;
	private Orientation mOrientation;
	private List<RotationAction> mActions;

	public Area getArea() {
		return mArea;
	}

	public void setArea(Area pArea) {
		mArea = pArea;
	}

	public Position getPosition() {
		return mPosition;
	}

	public void setPosition(Position pPosition) {
		mPosition = pPosition;
	}

	public Orientation getOrientation() {
		return mOrientation;
	}

	public void setOrientation(Orientation pOrientation) {
		mOrientation = pOrientation;
	}

	public List<RotationAction> getActions() {
		return mActions;
	}

	public void setRotationsActions(List<RotationAction> pActions) {
		mActions = pActions;
	}
}
