package se.fearlessgames.fear.collada.utils;

import org.jdom.Element;
import se.fearlessgames.fear.collada.data.AssetData;
import se.fearlessgames.fear.math.Vector3;

import java.util.List;

public class AssetDataParser {

	private final Element asset;

	public AssetDataParser(Element asset) {
		this.asset = asset;
	}

	@SuppressWarnings("unchecked")
	public AssetData parse() {

		final AssetData assetData = new AssetData();

		for (final Element child : (List<Element>) asset.getChildren()) {
			if ("contributor".equals(child.getName())) {
				parseContributor(assetData, child);
			} else if ("created".equals(child.getName())) {
				assetData.setCreated(child.getText());
			} else if ("keywords".equals(child.getName())) {
				assetData.setKeywords(child.getText());
			} else if ("modified".equals(child.getName())) {
				assetData.setModified(child.getText());
			} else if ("revision".equals(child.getName())) {
				assetData.setRevision(child.getText());
			} else if ("subject".equals(child.getName())) {
				assetData.setSubject(child.getText());
			} else if ("title".equals(child.getName())) {
				assetData.setTitle(child.getText());
			} else if ("unit".equals(child.getName())) {
				final String name = child.getAttributeValue("name");
				if (name != null) {
					assetData.setUnitName(name);
				}
				final String meter = child.getAttributeValue("meter");
				if (meter != null) {
					assetData.setUnitMeter(Float.parseFloat(meter.replace(",", ".")));
				}
			} else if ("up_axis".equals(child.getName())) {
				final String axis = child.getText();
				if ("X_UP".equals(axis)) {
					assetData.setUpAxis(Vector3.UNIT_X);
				} else if ("Y_UP".equals(axis)) {
					assetData.setUpAxis(Vector3.UNIT_Y);
				} else if ("Z_UP".equals(axis)) {
					assetData.setUpAxis(Vector3.UNIT_Z);
				}
			}
		}

		return assetData;
	}

	@SuppressWarnings("unchecked")
	private void parseContributor(final AssetData assetData, final Element contributor) {
		for (final Element child : (List<Element>) contributor.getChildren()) {
			if ("author".equals(child.getName())) {
				assetData.setAuthor(child.getText());
			} else if ("authoring_tool".equals(child.getName())) {
				assetData.setAuthoringTool(child.getText());
			} else if ("comments".equals(child.getName())) {
				assetData.setComments(child.getText());
			} else if ("copyright".equals(child.getName())) {
				assetData.setCopyright(child.getText());
			} else if ("source_data".equals(child.getName())) {
				assetData.setSourceData(child.getText());
			}
		}
	}

}
