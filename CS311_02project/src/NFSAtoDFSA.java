//Name: Pablo Leon
//Class: CS311-01
//Date: 11/13/15

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


public class NFSAtoDFSA {
	public static void main(String[] args) {
		//FILE PATH MUST BE CHANGED ACCORDING TO WHERE input.txt is located
		File file = new File("C:\\Users\\Pablo\\Desktop\\input.txt");
		int count = 1;
		try{
			Scanner scan = new Scanner(file);
			while (scan.hasNextLine()){
				System.out.println("Finite State Automaton #" + count );
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
				String[][]trans = new String[nstates][alpha.length+1];
				
				//the next lines to be read are the transitions which are singled out by being surrounded in parenthesis
				System.out.println("(4)Transitions: ");
				line = scan.nextLine();
				while(!line.equals(""))
				{
					String[] cords = line.split(delim);
					
					//place starting state in x
					String cord = cords[0];
					cord = "" + cord.charAt(1);
					int x = Integer.parseInt(cord);
					//input of current state
					String input = cords[1];
					int y = 0;
					for(int i = 0; i <alpha.length; i++)
					{
						if (input == "E")
						{
							y = alpha.length - 1;
							break;
						}
						if (input.equals(alpha[i]))
						{
							y = i;
						}
					}
					//with the starting state and input in hand we fill in the 2-D array with next state
					trans[x][y] = "";
					int i  = 2;
					while(true)
					{
						cord = cords[i];
						if (cord.charAt(cord.length() - 1) == ')')
						{
							if (i > 2)
								trans[x][y] = trans[x][y] + " " + cord.charAt(0);
							else
								trans[x][y] = trans[x][y] + cord.charAt(0);
							break;
						}
						if (i > 2)
						{
							trans[x][y] = trans[x][y]+ " " + cord;
						}
						else
							trans[x][y] = trans[x][y] + cord;
						i++;
					}
					
					System.out.println("\t" + x + " " + input + " " + trans[x][y]);
					line = scan.nextLine();
				}
				NFSAtoDFSA dfsa = new NFSAtoDFSA();
				dfsa.createDFSA(fstates,alpha,trans);
				count++;
			}
		}catch (FileNotFoundException e)
		{
			System.out.println("File was not found");
		}
	}

	public void createDFSA(boolean[]fstates, String[]alpha, String[][]trans)
	{
		int x = 0;
		//ArrayLists that holds list of states
		ArrayList<String> states = new ArrayList<String>();
		//ArrayList that holds String array with the transitions for DFSA
		ArrayList<String[]> cords = new ArrayList<String[]>();
		String delim = "\\s";
		states.add("0");
		//counter for number of states
		int c = 1;
		//string that holds new final states
		String nfstates = "";
		for(int i = 0; i < states.size(); i++)
		{
			//create an array of size alpha to hold the transitions for state
			String[] trans2 = new String[alpha.length];
			//add it to cords
			cords.add(trans2);
			//split it into strings that hold 1 state per string
			String[] parts = states.get(i).split(delim);
			for(int j = 0; j < parts.length; j++)
			{
				x = Integer.parseInt(parts[j]);
				for(int k = 0; k < alpha.length; k++)
				{
					//if there is no E Move add elements of 2D array to DFSA array
					if(trans[x][alpha.length] == null)
					{
						//if there is nothing in the string then
						if(trans2[k] == null)
						{
							if(trans[x][k] != null)
							{
								trans2[k] = trans[x][k];
							}
						}
						else
						{
							if(trans[x][k] != null)
							{
								//NEED TO CHECK IF STATE ALREADy IN TRANS2 DONT WANT REPEATS
								String[] current = trans2[k].split(delim);
								String[] future = trans[x][k].split(delim);
								for(int l = 0; l < future.length; l++)
								{
									boolean found = false;
									for(int m = 0; m < current.length; m++)
									{
										if(current[m].equals(future[l]))
											found = true;
									}
									if(found == false)
										trans2[k] =trans2[k] + " " + trans[x][k];
								}
							}
						}
					}
					//if there is E moves do this
					else
					{
						//ran into issues with the e-moves concept so didnt include it for now
					}
				}
			}
			//now that transition has been added check if its in cords, if not add it 
			for ( int k = 0; k < alpha.length; k ++)
			{
				if(trans2[k] != null)
				{
					String[] current = trans2[k].split(delim);
					boolean match = false;
				
					for(int l = 0; l < states.size(); l++)
					{
						String[] cState = states.get(l).split(delim);
						for(int m = 0; m < current.length; m++)
						{
							boolean found = false;
							for(int n = 0; n < cState.length; n++)
							{
								if(cState[n].equals(current[m]))
								{
									found = true;
									break;
								}
							}
							if (found == false)
							{
								match = false;
								break;
							}
							match = true;
						}
						if(match == true && current.length == cState.length)
							break;
						else
							match = false;
					}
					if(match == false)
					{
						states.add(trans2[k]);
						c++;
					}
				}
			}
		}
		System.out.println("The equivalent DFSA by subset construction:");
		System.out.println("1) Number of states: " + c);
		for (int i = 0; i < states.size(); i ++)
		{
			System.out.println("\tstate " + i + ": {" + states.get(i) + "}");
		}
		for(int i = 0; i < fstates.length; i ++)
		{
			if(fstates[i] == true)
			{
				String fstate = ""+ i;
				for( int j  = 0; j < states.size(); j ++)
				{
					String[] parts = states.get(j).split(delim);
					for (int k = 0; k < parts.length; k++)
					{
						if (fstate.equals(parts[k]))
							nfstates = nfstates + " " + j;
					}
				}
			}
		}
		System.out.println("2) Final States: " + nfstates);
		System.out.println("3) Transitions: ");
		for(int i = 0; i < cords.size(); i++ )
		{
			System.out.print("\tstate " + i + ": ");
			String[] output = cords.get(i);
			for (int j = 0; j < alpha.length; j++)
			{
				int num = 0;
				for(int k = 0; k < states.size();k++)
				{
					if (output[j].equals(states.get(k)))
						num = k;
				}
				System.out.print(alpha[j] + " " + num + ", ");
			}
			System.out.print("\n");
		}
	}
}
