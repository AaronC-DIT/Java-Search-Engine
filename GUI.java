package searchengine;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;
import static javax.swing.GroupLayout.Alignment.*;

public class GUI extends JFrame implements ActionListener {

		
	
		private static final long serialVersionUID = 1402802719826532559L;
		JLabel searchlabel = new JLabel("Search for : ");
		JTextField searchbar = new JTextField();
	    JFileChooser searchdirectory = new JFileChooser();
		JCheckBox matchexact = new JCheckBox("Match Exact");
		JCheckBox wildcards = new JCheckBox("Wildcards");
		JButton findbutton = new JButton("Find");
		GroupLayout mylayout = new GroupLayout(getContentPane());
	 
		
		public static void main(String[] args){
			
			new GUI();
		}
		
	 /*
	  *  Class Constructor
	  */
	 
	  public GUI(){
			
			 
		   setTitle("Search Engine Tool");
		   setSize(600,150);
		   setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		   setLocationRelativeTo(null);
		   
		   
		   /* 
		    * Layout Design and components, using 'GroupLayout'
		    * Documentation used: docs.oracle.org
		    */
		   
		   getContentPane().setLayout(mylayout);
		   mylayout.setAutoCreateGaps(true);
		   mylayout.setAutoCreateContainerGaps(true);
		   
		   /* 
		    * Horizontal layout
		   */
		   
		   mylayout.setHorizontalGroup(
					mylayout.createSequentialGroup()
						.addComponent(searchlabel)
						.addGroup(mylayout.createParallelGroup(LEADING)
								   .addComponent(searchbar)
								   .addGroup(mylayout.createSequentialGroup()
								              .addGroup(mylayout.createParallelGroup(LEADING)
								            		  .addComponent(matchexact))
								              .addGroup(mylayout.createParallelGroup(LEADING)
								                       .addComponent(wildcards))))
						.addGroup(mylayout.createParallelGroup(LEADING)
						        .addComponent(findbutton))  
					    );
		   
				
		   /*  
		    * Vertical layout
		   */
		   	   
		   mylayout.setVerticalGroup(
				   mylayout.createSequentialGroup()
				   .addGroup(mylayout.createParallelGroup(BASELINE)
			                .addComponent(searchlabel)
			                .addComponent(searchbar)
			                .addComponent(findbutton))
				   .addGroup(mylayout.createParallelGroup(LEADING)
			                .addGroup(mylayout.createSequentialGroup()
			                    .addGroup(mylayout.createParallelGroup(BASELINE)
			                    		.addComponent(matchexact)
			                    		.addComponent(wildcards))))
				
				   );   
		   
		  setVisible(true);
		  findbutton.addActionListener(this); 
		  
		}
	
	
	  public void actionPerformed(ActionEvent e){
		  if(e.getSource() == findbutton){
			  
			  String query = searchbar.getText();
			  
			  // Directory to search in
			  
			  searchdirectory.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			  searchdirectory.setDialogTitle("Select Folder");
			  int returnedDir = searchdirectory.showOpenDialog(null);
			  		if (returnedDir == JFileChooser.APPROVE_OPTION){
			  				
			  			
			  				// See if checkboxes are selected 
			  
			  			    boolean exactchecked = matchexact.isSelected();
			  				boolean wildcardschecked = wildcards.isSelected();
			  
			  				// Cannot have both selected at once -> Error 
			  
			  					if ((exactchecked) && (wildcardschecked)){
			  						JOptionPane.showMessageDialog(null, "Cannot selected both checkboxes at once, please try again", "Error", JOptionPane.ERROR_MESSAGE);
			  						
			  					} else if (exactchecked){
				  
			  						// Exact search process
			  						
			  						String[] filelist = getFiles(searchdirectory.getSelectedFile());
			  						
			  						searchFiles(filelist,query,1);
				  
				  
			  					} else if (wildcardschecked){
				 
			  						// Wildcard search process
			  						
			  						String[] filelist = getFiles(searchdirectory.getSelectedFile());
			  						
			  						searchFiles(filelist,query,2);
			  						
			  						
			  						
			  					} else {
			  						
			  						String[] filelist = getFiles(searchdirectory.getSelectedFile());
			  						
			  						searchFiles(filelist,query,0);
			  						
			  						
			  						
			  					}
			  					
			  			} else {
		   
			  				//No directory selected
		   
			  				JOptionPane.showMessageDialog(null, "No directory chosen", "Error", JOptionPane.ERROR_MESSAGE);

		   
			  			}
		  	}
		  
	  }
	  
	  //Method to retrieve all filenames within directory with full path
	  
	  public String[] getFiles(File directory){
		  
		  
		  File[] mylist = directory.listFiles();
		  Collection<String> filearray = new ArrayList<String>();
		  
		  		for (File file : mylist){
		  			if (file.isFile()){
		  				filearray.add(file.getAbsolutePath());
		  			}
		  		}
		  
		  	return filearray.toArray(new String[]{});
		  
	  }
	  
	  // Method to search for text in files
	  	  
	  public void searchFiles(String[] filelist, String query, int option){
		  
		  query = query.trim();
		  String currentline;
		  Collection<String> filesfound = new ArrayList<String>();
		  FileReader freader;
		  BufferedReader bfreader; //Average 10 times faster than Scanner
		  String regex = null;
		  Pattern pattern = null;
		  Matcher matcher;
		  
			
		  if (option == 1){
			  
			  //Exact search regex setup
			  
			 regex = "\\b"+query+"\\b";
			 pattern = Pattern.compile(regex);
			  
		  }
		 
		  
		  
		  //Iterate through each file while searching for text
		  
		  try {
			  	for (String filename : filelist){
			  
			  		freader = new FileReader(filename);
		  			bfreader = new BufferedReader(freader);
		  			
		  			
		  				while(((currentline = bfreader.readLine()) != null)){
		  				
		  					/*
		  					 * Option modifiers 
		  					 * if 0, standard search
		  					 * if 1, exact search
		  					 * if 2, wildcard search
		  					 * 
		  					 */
		  					
		  					if (option == 1){
		  						
		  						//Exact match search
		  						
		  						matcher = pattern.matcher(currentline);
		  							if (matcher.find() == true){
		  							
		  								filesfound.add(filename);
		  							} 
		  						
		  					else if (option == 2) {
		  						
		  						if (currentline.contains(query)){
			  						
		  							filesfound.add(filename);	//append filename to arraylist if found

		  							} 
		  						
		  						}
		  							
		  					} else {
		  						
		  					
		  						if (currentline.contains(query)){
		  						
		  							filesfound.add(filename);	//append filename to arraylist if found
		  						}
		  					}
		  					
		  				}
		  			
			  		}
			  	
		  } catch(FileNotFoundException exception1){
			  JOptionPane.showMessageDialog(null,"Could not find file","Error", JOptionPane.ERROR_MESSAGE);
		  	} 
		  	catch (IOException exception2){
			  JOptionPane.showMessageDialog(null,"Failed","Error", JOptionPane.ERROR_MESSAGE);
		  	}
			  
		  
		  /*
		   * Show results
		   *  
		   * Variables:
		   *  - duplicatefree, a set of strings converted from our filesfound in order to remove duplicate results
		   *  
		   */
		  
		  Set<String> duplicatefree = new HashSet<String>(filesfound);
		  if (duplicatefree.isEmpty()){
			  
			  JOptionPane.showMessageDialog(null,"There are no matches","Results", JOptionPane.INFORMATION_MESSAGE);

			  
		  } else {
		  JOptionPane.showMessageDialog(null,String.join("\n", duplicatefree),"Results", JOptionPane.INFORMATION_MESSAGE);
		  }
	  }
}
	  
