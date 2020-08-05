package heap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class dbLoad {
	
	static int pageSize;
	static int numOfBuilding = 0;
	static int numOfPages = 0;
	static long startTime = 0;
	static long endTime = 0;
	

	static ArrayList<Page> heap = new ArrayList<Page>();
	public static void main(String[] args) {
		//File f = new File("FormattedDataWithCounterNewNoNull.csv");
		startTime = System.currentTimeMillis();
		if(args.length >= 3 ){
			if(args[0].equals("-p")){
				File f = new File(args[2]);
				try{
					pageSize = Integer.parseInt(args[1]);
				} catch(NumberFormatException e){
					pageSize = 4096;
					
					System.out.println("Pagesize was not a number defaulting to 4096");
				}
				
				try{
					System.out.println("Reading File");
					System.out.println(pageSize);
					readLines(f);
				}
				catch(FileNotFoundException fnf){
					System.out.println(fnf.getMessage());
					System.out.println("Terminating Program");
					System.exit(0);
				}
				catch(IOException e){
					e.printStackTrace();
				}
				System.out.println("Finished Reading File");
				System.out.println("Writing to heap file");
				writeLines();
				System.out.println("Finsihed Writing File");
				endTime = System.currentTimeMillis();
				stdout();

			}
		}
		else{
			System.out.println("Run format : java dbload -p [pagesize] [file]");
		}
		
	}
	
	public static void readLines(File f) throws IOException{
		Building building = null;
		Page page = new Page(pageSize);
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		String line;
		br.readLine();
		
		while((line = br.readLine()) != null){
		
			String[] values = line.split("#",-1);

			building = new Building(Integer.parseInt(values[0]),Integer.parseInt(values[1]),
					Integer.parseInt(values[2]),Integer.parseInt(values[3]),Integer.parseInt(values[9]),values[6],values[10]);
		

			if(!values[4].isEmpty()) {
				building.setBuildingName(values[4]);
			}
			if(!values[5].isEmpty()) {
				building.setStreetAddr(values[5]);
			}
		
			if(!values[7].isEmpty()) {
				int constructYear = Math.round(Float.parseFloat(values[7]));
				building.setConstructYear(constructYear);
			}
			if(!values[8].isEmpty()) {
				int refurbishYear = Math.round(Float.parseFloat(values[8]));
				building.setRefurbishYear(refurbishYear);
			}
			if(!values[11].isEmpty()) {
				building.setAccessType(values[11]);
			}
			if(!values[12].isEmpty()) {
				building.setAccessDesc(values[12]);
			}
			if(!values[13].isEmpty()) {
				
				building.setAccessRating(Math.round(Float.parseFloat(values[13])));
			}
			if(!values[14].isEmpty()) {	
//				System.out.println(values[2]);
//				System.out.println(values[14]);
				building.setHasBicycle(Math.round(Float.parseFloat(values[14])));
			}
			if(!values[15].isEmpty()) {
				building.setHasShower(Boolean.parseBoolean(values[15]));
			}
			if(!values[16].isEmpty()) {
				building.setxCoord(Double.parseDouble(values[16]));
			}
			
			if(!values[17].isEmpty()) {
				building.setyCoord(Double.parseDouble(values[17]));
			}
			if(!values[18].isEmpty()) {
				building.setLocation(values[18]);
			}
			
			if(building.byteAllocation()+page.getPageSize() < pageSize){
				page.addBuildings(building);
				numOfBuilding++;

			}else{
				heap.add(page);
				page = new Page(pageSize);
				numOfPages++;
				page.addBuildings(building);
				numOfBuilding++;
			}
		}
		heap.add(page);
		br.close();
		fr.close();
		
	}
	
	public static void writeLines(){
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try{
			fos = new FileOutputStream("heap."+pageSize);
			oos = new ObjectOutputStream(fos);
			System.out.println(numOfBuilding);
			for(Page p:heap){
				if(p != null){
					oos.writeObject(p);
				}
			}
			fos.close();
			oos.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static void stdout(){
		System.out.println("Number of Buildings: " + numOfBuilding);
		System.out.println("Number of Pages: " + numOfPages);
		System.out.println("Time Taken: " + (endTime - startTime) +"ms");
	}

}
