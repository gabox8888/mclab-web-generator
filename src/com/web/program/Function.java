package com.web.program;

public class Function implements Writable {
	
	private String aName;
	private List<>
	
	/**
	 * 
	 * @param pName
	 */
	public Function(String pName) {
		aName = pName;
	}

	/**
	 * @return 
	 */
	@Override
	public String[] toFile() {
		return null;
	}

	public static void main(String[] args) {
		
		Header test = new Header();
		test.addRepositoryModule("ExtractFunction");
		test.addStandardModule("q","Q");
		test.addRepositoryModule("ExtractVariable");
		test.addStandardModule("path");
		for(String s : test.toFile()) {
			System.out.println(s);
		}

	}

}
