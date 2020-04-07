import java.io.*;

public class UnixTree 
{
	Node currentNode;
	Node root;
	
	public UnixTree()
	{
		root = this.new Node("root", true, null, "D");
		currentNode = root;
	}
	
	public void processCommand(String cmd)
	{
		String[] cmdString = cmd.trim().split("\\s+");
		switch(cmdString[0]) 
		{
			case "mkdir":
				System.out.format("$%s %s\n",cmdString[0],cmdString[1]);
				if (doesNodeExist(cmdString[1])) {
					System.out.format("D %s already exists...\n", cmdString[1]);
				} else {
					insertDirectory(cmdString[1]);
					System.out.format("%s\n", cmdString[1]);
				}
				break;
			case "ls":
				System.out.format("$%s\n", cmdString[0]);
				printDirContents();
				break;
			case "cd":
				System.out.format("$%s %s\n",cmdString[0], cmdString[1]);
				int changeDirReportCode = changeDir(cmdString[1]);
				if (changeDirReportCode == 0) {
					System.out.format("%s is not located in %s\n",cmdString[1],currentNode.nodeName);
				} else if (changeDirReportCode == 1) {
					System.out.format("%s is not a directory\n", cmdString[1]);
				} else {
					printCurrentPath();
				}
				break;
			case "pwd":
				System.out.format("$%s\n",cmdString[0]);
				printCurrentPath();
				break;
			case "addf":
				System.out.format("$%s %s\n",cmdString[0],cmdString[1]);
				if (doesNodeExist(cmdString[1])) {
					System.out.format("F %s already exists...\n", cmdString[1]);
				} else {
					addFile(cmdString[1]);
				}
				break;
			case "mv":
				System.out.format("$%s %s %s\n", cmdString[0],cmdString[1],cmdString[2]);
				if(doesNodeExist(cmdString[1])) {
					changeNodeName(cmdString[1], cmdString[2]);
				} else {
					System.out.format("%s does not exist, cannot change the name\n", cmdString[1]);
				}
				break;
			case "rm":
				System.out.format("$%s %s\n", cmdString[0],cmdString[1]);
				if (doesNodeExist(cmdString[1])) {
					removeNode(cmdString[1]);
				} else {
					System.out.format("%s is not in ", cmdString[1]);
					printCurrentPath();
				}
				break;
			default:
				break;
		}
	}
	
	public boolean doesNodeExist(String name)
	{
		boolean exist = false;
		if (currentNode.left == null) {
			exist = false;
		} else {
			Node tempFuncNode = currentNode.left;
			while (tempFuncNode != null)
			{
				if (tempFuncNode.nodeName.contentEquals(name)) {
					exist = true;
					break;
				} else {
					tempFuncNode = tempFuncNode.nextNode;
				}
			}
		}
		
		return exist;
	}
	
	public void insertDirectory(String dirName)
	{
		Node newNode = this.new Node(dirName, true, currentNode, "D");
		if (currentNode.left == null) {
			currentNode.left = newNode;
		} else {
			Node tempFuncNode = currentNode.left;
			if (tempFuncNode.nextNode == null) {
				tempFuncNode.nextNode = newNode;
			} else {
				while (tempFuncNode.nextNode != null) 
				{
					tempFuncNode = tempFuncNode.nextNode;
				}
				tempFuncNode.nextNode = newNode;
			}
		}
	}
	
	public void printDirContents()
	{
		Node tempFuncNode = currentNode;
		if (tempFuncNode.left == null) {
			System.out.print("  \n");
		} else {
			tempFuncNode = tempFuncNode.left;
			System.out.format("%s %s\n", tempFuncNode.identifier, tempFuncNode.nodeName);
			while (tempFuncNode.nextNode != null) 
			{
				tempFuncNode = tempFuncNode.nextNode;
				System.out.format("%s %s\n", tempFuncNode.identifier, tempFuncNode.nodeName);
			}
		}
	}
	
	public int changeDir(String dirName)
	{
		/*
		 * reportCode 0 - directory is not in current directory
		 * reportCode 1 - directory is not actually a directory
		 * reportCode 2 - directory change successful
		 */
		int reportCode = 0; // initialize to unsuccessful change
		
		if (dirName.contentEquals("..")) {
			if (currentNode.parentNode != null) {
				currentNode = currentNode.parentNode;
			}
			reportCode = 2;
		} else {
			if (currentNode.left != null) {
				Node tempFuncNode = currentNode.left;
				 while (tempFuncNode != null)
				 {
					if (tempFuncNode.nodeName.contentEquals(dirName)) {
						if (tempFuncNode.isDirectory == true) {
							reportCode = 2;
							currentNode = tempFuncNode;
							break;
						} else {
							reportCode = 1;
							break;
						}
					} else {
						tempFuncNode = tempFuncNode.nextNode;
					}
				}
			}
		}
		return reportCode;
	}
	
	public void printCurrentPath()
	{
		Node tempFuncNode = currentNode;
		if (currentNode.parentNode == null) {
			System.out.format("/%s\n", currentNode.nodeName);
		} else {
			String dirs = ""; // string to add directories to
			// traverse backwards from current node through its parents
			while (tempFuncNode.parentNode != null) 
			{
				dirs += "/"; // add slash to split dir names later
				dirs += tempFuncNode.nodeName;
				tempFuncNode = tempFuncNode.parentNode;
			}
			// add final parent node (root) to the string
			dirs += "/";
			dirs += tempFuncNode.nodeName;
			/* dir names were added in reverse, so we print them from highest value to value 0
			 * highest value is current node and lowest value 0 is /root
			 */
			String[] path = dirs.split("/",0);
			for (int i = path.length - 1; i > 0; i--) {
				System.out.format("/%s",path[i]);
			}
			System.out.print("\n");
		}
	}
	
	public void addFile(String fileName)
	{
		Node newNode = this.new Node(fileName, false, currentNode, "F");
		if (currentNode.left == null) {
			currentNode.left = newNode;
		} else {
			Node tempFuncNode = currentNode.left;
			if (tempFuncNode.nextNode == null) {
				tempFuncNode.nextNode = newNode;
			} else {
				while (tempFuncNode.nextNode != null) 
				{
					tempFuncNode = tempFuncNode.nextNode;
				}
				tempFuncNode.nextNode = newNode;
			}
		}
	}
	
	public void changeNodeName(String nodeName, String nameChange)
	{
		Node tempFuncNode = currentNode.left;
		while (tempFuncNode != null)
		{
			if (tempFuncNode.nodeName.contentEquals(nodeName)) {
				tempFuncNode.nodeName = nameChange;
				break;
			} else {
				tempFuncNode = tempFuncNode.nextNode;
			}
		}
	}
	
	public void removeNode(String removeNode)
	{
		Node tempFuncNode = currentNode.left;
		Node nodeToRemove = null;
		if (tempFuncNode.nodeName.contentEquals(removeNode)) {
			nodeToRemove = tempFuncNode;
			tempFuncNode = nodeToRemove.nextNode;
			nodeToRemove.left = null;
			nodeToRemove.nextNode = null;
			currentNode.left = tempFuncNode;
		} else {
			while (!(tempFuncNode.nextNode.nodeName.contentEquals(removeNode)))
			{
				tempFuncNode = tempFuncNode.nextNode;
			}
			nodeToRemove = tempFuncNode.nextNode;
			tempFuncNode.nextNode = nodeToRemove.nextNode;
			nodeToRemove.left = null;
			nodeToRemove.nextNode = null;
		}
	}
	
	public class Node 
	{
		private String nodeName;
		private String identifier;
		private boolean isDirectory;
		private Node parentNode;
		private Node nextNode;
		private Node left;
		
		public Node(String name, boolean isDir, Node parent, String identity)
		{
			this.nodeName = name;
			this.identifier = identity;
			this.isDirectory = isDir;
			this.parentNode = parent;
			this.left = null;
			this.nextNode = null;
		}
	}

}
