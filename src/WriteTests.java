	
	

	import java.io.BufferedWriter;
	import java.io.FileWriter;
	import java.io.IOException;

	public class WriteTests {
		public static void writeTests(String nameTo) {
			if (!nameTo.endsWith(".txt")) {
				nameTo = nameTo + ".txt";
			}
			try {
				String name = nameTo;
				FileWriter fw = null;
				BufferedWriter bw = null;
				fw = new FileWriter(name);
				bw = new BufferedWriter(fw);
				int numberOfNodes = 1000;
				double arr[][]= new double[1000][1000];
				for (int i=0; i<1000;i++)
					for (int j=0; j<1000; j++)
						arr[i][j]=-1;
				bw.write(numberOfNodes + "\n");
				bw.write(3000 + "\n");
				for (int i = 0; i < 3000;) {
					int ni=(int)(Math.random()*1000);
					int nj=(int)(Math.random()*1000);
					if (arr[ni][nj]==-1){
						double num = Math.random()*1000;
						arr[ni][nj]=num;
						arr[nj][ni]=num;
						bw.write(ni + " "+ nj + " " + num + "\n");
						i++;
					}
			
				}
				bw.close();
				fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		public static void main(String[] args) {
				writeTests("test"+ 2);
		}
	}


