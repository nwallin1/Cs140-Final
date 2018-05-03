package project;

public class Memory {

	public static final int DATA_SIZE = 2048;
	private int[] data = new int[DATA_SIZE];
	public static final int CODE_MAX = 2048;
	private int[] code = new int[CODE_MAX];
	private int changedIndex = -1;
	
	int[] getCode() {
		return code;
	}
	
	public int getOp(int i) {
		return code[2*i];
	}
	
	public int getArg(int i) {		
		return code[2*i + 1];
	}
	
	public void clear(int start, int end) {
		for(int i = start; i < end; i++) {
			code[2*i]=0;
			code[2*i+1]=0;
		}
	}
	
	public void clearData(int start, int end) {
		for(int i = start; i < end; i++) {
			data[i] = 0;
		}
		
		changedIndex = -1;
	}
	
	public void clearCode(int start, int end) {
		for(int i = start; i < end; i++) {
			code[i] = 0;
		}
		
		changedIndex = -1;
	}
	
	public void setCode(int index, int op, int arg) {
		code[2*index] = op;
		code[2*index+1] = arg;
	}
	
	int[] getData() {
		return data;
	}
	
	public int getData(int index) {
		return data[index];
	}
	
	public void setData(int index, int value) {
		data[index] = value;
		changedIndex = index;
	}
	
	public int getChangedIndex() {
		return changedIndex;
	}
}