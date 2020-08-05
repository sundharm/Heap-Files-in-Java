package heap;

import java.io.Serializable;
import java.util.ArrayList;

public class Page implements Serializable {
	private static final long serialVersionUID = 6529685098267757690L;
	int pageNum = 0;
	int pageSize = 0;
	ArrayList<Building> buildings = new ArrayList<Building>();
	

	public Page(int size){
		pageSize = size;
		pageNum++;
	}
	public void addBuildings(Building loadLine){
		buildings.add(loadLine);
	}

	public ArrayList<Building> getBuildings(){
		return buildings;
	}

	public int getPageSize(){
		int pageSize = 0;
		for(Building building : buildings){
			pageSize += building.byteAllocation();
		}
		return pageSize;
	}	
}
