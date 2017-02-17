import java.util.ArrayList;
import java.util.HashMap;
	 
public class kNN {
	public static void main(String[] args){
		ArrayList<kNN.DataEntry> data = new ArrayList<kNN.DataEntry>();
		data.add(new DataEntry(new double[]{175,80,50}, "Male"));
		data.add(new DataEntry(new double[]{193.5,110,70}, "Male"));
		data.add(new DataEntry(new double[]{183,92.8, 50}, "Male"));
		data.add(new DataEntry(new double[]{160,60, 70}, "Male"));
		data.add(new DataEntry(new double[]{177,73.1, 60}, "Male"));
		data.add(new DataEntry(new double[]{175,80, 20}, "Female"));
		data.add(new DataEntry(new double[]{150,55, 30}, "Female"));
		data.add(new DataEntry(new double[]{159,63.2, 25}, "Female"));
		data.add(new DataEntry(new double[]{180,70, 25}, "Female"));
		data.add(new DataEntry(new double[]{163,110, 30}, "Female"));
		kNN nn = new kNN(data, 3); //3 neighbours
		System.out.println("Classified as: "+nn.classify(new DataEntry(new double[]{170, 60,70},"Ignore")));
	}
	
	
	private int k;
	private ArrayList<Object> classes;
	private ArrayList<DataEntry> dataSet;
	/**
	 * 
	 * @param dataSet The set
	 * @param k The number of neighbours to use
	 */
	public kNN(ArrayList<DataEntry> dataSet, int k){
		this.classes = new ArrayList<Object>();
		this.k = k;
		this.dataSet = dataSet;
		
		//Load different classes
		for(DataEntry entry : dataSet){
			if(!classes.contains(entry.getY())) classes.add(entry.getY());
		}
	}
	
	private DataEntry[] getNearestNeighbourType(DataEntry x){
		DataEntry[] retur = new DataEntry[this.k];
		double fjernest = Double.MIN_VALUE;
		int index = 0;
		for(DataEntry tse : this.dataSet){
			double distance = distance(x,tse);
			if(retur[retur.length-1] == null){ //Hvis ikke fyldt
				int j = 0;
				while(j < retur.length){
					if(retur[j] == null){
						retur[j] = tse; break;
					}
					j++;
				}
				if(distance > fjernest){
					index = j;
					fjernest = distance;
				}
			}
			else{
				if(distance < fjernest){
					retur[index] = tse;
					double f = 0.0;
					int ind = 0;
					for(int j = 0; j < retur.length; j++){
						double dt = distance(retur[j],x);
						if(dt > f){
							f = dt;
							ind = j;
						}
					}
					fjernest = f;
					index = ind;
				}
			}
		}
		return retur;
	}
	
	private static double convertDistance(double d){
		return 1.0/d;
	}

	/**
	 * Computes Euclidean distance
	 * @param a From
	 * @param b To
	 * @return Distance 
	 */
	public static double distance(DataEntry a, DataEntry b){
		double distance = 0.0;
		int length = a.getX().length;
		for(int i = 0; i < length; i++){
			double t = a.getX()[i]-b.getX()[i];
			distance = distance+t*t;
		}
		return Math.sqrt(distance);
	}
	/**
	 * 
	 * @param e Entry to be classifies
	 * @return The class of the most probable class
	 */
	public Object classify(DataEntry e){
		HashMap<Object,Double> classcount = new HashMap<Object,Double>();
		DataEntry[] de = this.getNearestNeighbourType(e);
		for(int i = 0; i < de.length; i++){
			double distance = kNN.convertDistance(kNN.distance(de[i], e));
			if(!classcount.containsKey(de[i].getY())){
				classcount.put(de[i].getY(), distance);
			}
			else{
				classcount.put(de[i].getY(), classcount.get(de[i].getY())+distance);
			}
		}
		//Find right choice
		Object o = null;
		double max = 0;
		for(Object ob : classcount.keySet()){
			if(classcount.get(ob) > max){
				max = classcount.get(ob);
				o = ob;
			}
		}
		
		return o;
	}

public static class DataEntry{
	private double[] x;
	private Object y;
	
	public DataEntry(double[] x, Object y){
		this.x = x;
		this.y = y;
	}
	
		public double[] getX(){
			return this.x;
		}
	
		public Object getY(){
			return this.y;
		}
	}
}
