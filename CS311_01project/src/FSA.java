import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class FSA {

	public static void main(String[] args) throws IOException {
		File file = new File("C:\\Users\\Pablo\\Desktop\\FSA.txt");
		try{
			Scanner scan = new Scanner(file);
			while (scan.hasNextLine()){
				
				//read first line and enter number of states
				String line = scan.nextLine();
				
				//number of states
				int nstates = Integer.parseInt(line);
				System.out.println("(1)Number of States: " + nstates);
				
				//array of states and whether they are final or not
				//set the length of fstates to nstates and initialize to false
				boolean[] fstates = new boolean[nstates];
				Arrays.fill(fstates, false);
				
				//read next line that has list of final states
				line = scan.nextLine();
				String delim = "\\s";
				String[] parts = line.split(delim);
				System.out.print("(2)Final States:");
				for (int i = 0; i < parts.length; i++)
				{
					fstates[Integer.parseInt(parts[i])] = true;
					System.out.print(Integer.parseInt(parts[i]) + ", ");
				}
				System.out.print("\n");
				
				//scan the next line with the list of alphabet and store it in alpha
				line = scan.nextLine();
				String[] alpha = line.split(delim);
				System.out.print("(3)Alphabet: ");
				for(int i = 0; i < alpha.length; i++)
				{
					System.out.print(alpha[i] + ", ");
				}
				System.out.println("");
				
				//create 2-D array with height nstates and width alpha.length
				String[][] trans = new String[nstates][alpha.length];
				for (int i = 0; i < nstates; i++)
				{
					for(int j = 0; j < alpha.length; j++)
					{
						trans[i][j] = "-1";
					}
				}
				//the next lines to be read are the transitions which are singled out by being surrounded in parenthesis
				System.out.println("(4)Transitions: ");
				while(true)
				{
					line = scan.nextLine();
					String[] cords = line.split(delim);
					//if the string in the first cell of cords is '(' perform this function
					if(cords[0].equals("("))
					{
						//look trough alpha array for matching string in cords[2] when found use its position plus
						//state number in cords[1] to fill in the transition in cords[3] into the trans 2D array
						for(int i = 0; i < alpha.length; i++)
						{
							if(alpha[i].equals(cords[2]))
							{
								int input = i;
								trans[Integer.parseInt(cords[1])][input]=cords[3];
								System.out.println(cords[1] + " " + cords[2] + " " + cords[3]);
								break;
							}
						}
					}
					else
						break;
				}
				//trans 2D array is made 
				//loop that will go trough all test strings and determine if they are accepted or rejected
				System.out.println("(5) Strings:");
				while(true)
				{
					int iState = 0;
					boolean exit = false;
					System.out.print("test string " + line);
					if (line.equals(""))
					{
						System.out.println("Done testing all String values. End of automata.\n");
						break;
					}
					else
					{
						//split test string into individual strings
						String[] spLine = line.split("(?!^)");
						//for all items in test string
						for(int i = 0; i < spLine.length; i++)
						{
							//compare character in string to make sure its part of the alphabet
							for (int j = 0; j < alpha.length; j++)
							{
								//if it is part of the alphabet then
								if (spLine[i].equals(alpha[j]))
								{
									//get next state do to transition
									if(Integer.parseInt(trans[iState][j]) == -1)
									{
										exit = true;
										break;
									}
									else
									{
										iState = Integer.parseInt(trans[iState][j]);
										break;
									}
								}
								//string not found in alphabet
								else
								{
									System.out.print("\n" + spLine[i] + " not in alphabet");
									exit = true;
								}
							}
							if ( exit == true)
							{
								System.out.print(" Rejected\n");
								break;
							}
							//if last item of string has been checked and is in alphabet
							if(i == spLine.length - 1)
							{
								// check if iState is a final state
								if (fstates[iState] == true)
									System.out.println(" Accepted\n");
								else
									System.out.println(" Rejected\n");
							}
						}
					}
					line = scan.nextLine();
				}
				
			}
		}catch (FileNotFoundException e)
		{
			System.out.println("File was not found");
		}
	}

}
