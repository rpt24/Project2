import java.io.*;

public class Project2Main 
{
	public static void main(String[] args) throws Exception
	{
		/* TODO - delete this comment
		 * Under Run Configurations...
		 * Tab "Arguments"
		 * The "Working Directory" is set to /Project2/src so it can read this file
		 */
		File file = new File("commands.txt");
		BufferedReader br = new BufferedReader(new FileReader(file)); 
		
		UnixTree myTree = new UnixTree();
		
		String st;
		while((st = br.readLine()) != null)
		{
			if (st.contentEquals("bye")) {
				break;
			} else {
				myTree.processCommand(st);	
			}
		}
		br.close();
	}
}
