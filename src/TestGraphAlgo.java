import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.InputMismatchException;

public class TestGraphAlgo {

	int defualtStartVertex = 0;
	String answerFileName = "D:\\answer1.txt";
	Graph_algo graphAlgo;
	
	public TestGraphAlgo(String fileName){
		File file= new File(fileName);
        Graph graph = new Graph(file);
       this.graphAlgo = new Graph_algo(graph, defualtStartVertex);
       
	}
	
	public void RunTestFile (String testFileName){
		String answer;
		PrintWriter writerToAnswerFile = CreatNewAnswerFile (answerFileName);
		File file= new File(testFileName);
        In in = new In(file);
        int queriesNumber = in.readInt();
        for (int i = 0; i < queriesNumber; i++) {
        	try{
        		int start = in.readInt();
        		int end = in.readInt();
        		int numOfBlackList =in.readInt();
        		int arrBlackList [] = new int [numOfBlackList];
        		for (int j = 0; j < numOfBlackList; j++) {
        			arrBlackList[j]= in.readInt();
				}
				answer= getAnswer(start, end, numOfBlackList, arrBlackList);
				writerToAnswerFile.println(answer);	

        	}
			catch (InputMismatchException e){
				answer = getInfo();
				writerToAnswerFile.println(answer);	
			}
		}
	    writerToAnswerFile.close();
	    System.out.println("done!!");
	}
	
	private String getInfo() {
		return graphAlgo.getInfo();
	}

	private String getAnswer(int start, int end, int numOfBlackList, int[] arrBlackList) {
		String ans = ""+start +" "+ end +" "+ numOfBlackList +" "+ PrintArray(arrBlackList) +" ";
		if(numOfBlackList == 0){
			ans  = ans + graphAlgo.distAToB(start, end);
		}
		else{
			ans  = ans + graphAlgo.distAToB_WithBlackList(start, end, arrBlackList);
		}
		return ans;
	}

	private String PrintArray(int[] arrBlackList) {
		String ans = "";
		for (int i = 0; i < arrBlackList.length; i++) {
			ans = ans + arrBlackList[i];
		}
		return ans;
	}

	private PrintWriter CreatNewAnswerFile (String name){
		try{
		    PrintWriter writer = new PrintWriter(name, "UTF-8");
		    return writer;
		} catch (IOException e) {
			return null;
		}
	}
	
	public static void main(String[] args) {
		TestGraphAlgo test = new TestGraphAlgo("c:\\G0.txt");
		test.RunTestFile("D:\\test1.txt");
		
	}
}
