package com.health.loader;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.health.data.BodyRegion;
import com.health.data.CommonFeelingGroup;
import com.health.data.FactorGroup;
import com.health.data.IGridGroup;
import com.health.data.IGridItem;

public class BodyFeelingTypeData {

	// CommonFeeling
	private List<CommonFeelingGroup> commonFeelingGroups;

	private LinkedHashMap<IGridGroup, List<IGridItem>> commonFeelingTypeHashMap;

	// Factor
	private List<FactorGroup> factorGroups;

	private LinkedHashMap<IGridGroup, List<IGridItem>> factorTypeHashMap;

	public List<CommonFeelingGroup> getCommonFeelingGroups() {
		return commonFeelingGroups;
	}

	public void setCommonFeelingGroups(List<CommonFeelingGroup> commonFeelingGroups) {
		this.commonFeelingGroups = commonFeelingGroups;
	}

	public HashMap<IGridGroup, List<IGridItem>> getCommonFeelingTypeHashMap() {
		return commonFeelingTypeHashMap;
	}

	public void setCommonFeelingTypeHashMap(LinkedHashMap<IGridGroup, List<IGridItem>> commonFeelingTypeHashMap) {
		this.commonFeelingTypeHashMap = commonFeelingTypeHashMap;
	}

	public List<FactorGroup> getFactorGroups() {
		return factorGroups;
	}

	public void setFactorGroups(List<FactorGroup> psyhologicalFeelingGroups) {
		this.factorGroups = psyhologicalFeelingGroups;
	}

	public HashMap<IGridGroup, List<IGridItem>> getFactorTypeHashMap() {
		return factorTypeHashMap;
	}

	public void setFactorTypeHashMap(LinkedHashMap<IGridGroup, List<IGridItem>> factorTypeHashMap) {
		this.factorTypeHashMap = factorTypeHashMap;
	}

}
