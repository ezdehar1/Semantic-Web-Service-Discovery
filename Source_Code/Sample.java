package t33_a;

import java.util.stream.Collectors;
import edu.cmu.lti.lexical_db.ILexicalDatabase;

import edu.cmu.*;
import edu.cmu.lti.jawjaw.util.*;



import edu.cmu.lti.lexical_db.NictWordNet;


import edu.cmu.lti.ws4j.RelatednessCalculator;
import edu.cmu.lti.ws4j.impl.HirstStOnge;
import edu.cmu.lti.ws4j.impl.JiangConrath;
import edu.cmu.lti.ws4j.impl.LeacockChodorow;
import edu.cmu.lti.ws4j.impl.Lesk;
import edu.cmu.lti.ws4j.impl.Lin;
import edu.cmu.lti.ws4j.impl.Path;
import edu.cmu.lti.ws4j.impl.Resnik;
import edu.cmu.lti.ws4j.impl.WuPalmer;
import edu.cmu.lti.ws4j.util.WS4JConfiguration;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
 
import java.sql.ResultSet;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.aspose.cells.Workbook;
import com.aspose.cells.Worksheet;
import com.aspose.cells.WorksheetCollection;
	
	public class Sample  {
		
		private static	List<String> Service_Names=new ArrayList<String>();  
		private static	List<String> Input_parm=new ArrayList<String>();  
		private static	List<String> Output_parm=new ArrayList<String>();
		
		
	
		private static ILexicalDatabase db = new NictWordNet();
		  
		  private static double compute(String word1, String word2) {
				WS4JConfiguration.getInstance().setMFS(true);
				double s = new WuPalmer (db).calcRelatednessOfWords(word1, word2);
				return s;
			}
		  
		  
		  private static double Sim_cal(String ser_name, String Ip,String Op,List<String> ser,List<String> IL,List<String>OL) {
				
				//Pattern pattern = Pattern.compile("w3schools", Pattern.CASE_INSENSITIVE);
			    //Matcher matcher = pattern.matcher("Visit W3Schools!");
			   // boolean matchFound = matcher.find();
			  boolean matchFound=false;
			    
			    for (int i = 0; i < ser.size(); i++) {
			    	//System.out.println(ser_name);
			    	 //System.out.println(ser.get(i));
			    	Pattern pattern = Pattern.compile(ser_name, Pattern.CASE_INSENSITIVE); 
			    	 Matcher matcher = pattern.matcher(ser.get(i));
			    	  matchFound = matcher.find();
			    	  System.out.println(matchFound);
			    	 
			}
			    if(matchFound)
			    	 System.out.println("found");
			    else
			    	 System.out.println("Not found");
			    return 0.0;
		  }
		//  ======================================================================
		  
		  private static List<service> Extract_Services(String iPF)  throws Exception
		  {
			  
			  
			  
			 
			// Load Excel file
				Workbook wb= new Workbook(iPF);	
				boolean matchFound=false;
				List<service> ServiceList=new ArrayList<service>();  
				
				// Get all worksheets
				
				Worksheet WSNa;
				Worksheet WSIP;
				Worksheet WSOP;
				
				WorksheetCollection collection = wb.getWorksheets();
				WSNa= collection.get(0);
				WSIP= collection.get(1);
				WSOP=collection.get(2);
                
				
				   int Sno = WSNa.getCells().getMaxDataRow();
				   int PIno= WSIP.getCells().getMaxDataRow();
				   int POno= WSOP.getCells().getMaxDataRow();
				// Loop through all the worksheets
				for (int Index = 1; Index <=Sno; Index++)
				{  
					service s=new service();
					s.name=WSNa.getCells().get(Index, 0).getValue().toString();
					s.description=WSNa.getCells().get(Index, 1).getValue().toString();
					int pos=s.name.indexOf('#');
					s.name=s.name.substring(1,pos);
					System.out.println("serrr "+s.name);
					   
					for (int indexParI = 1; indexParI <=PIno; indexParI++)
					{
						
						//WSIP.getCells().get(indexParI, 0).getValue().toString()
						//System.out.println("parm "+WSIP.getCells().get(indexParI, 0).getValue().toString());
						Pattern pattern = Pattern.compile(s.name , Pattern.CASE_INSENSITIVE); 
				    	 Matcher matcher = pattern.matcher(WSIP.getCells().get(indexParI, 0).getValue().toString());
				    	  matchFound = matcher.find();
				    	 if(matchFound) {
				    		s.InP.add(WSIP.getCells().get(indexParI, 0).getValue().toString()) ;
				    	     System.out.println("parm "+WSIP.getCells().get(indexParI, 0).getValue().toString());}
						
					}
					
					
					
					for (int indexParO = 1; indexParO <= POno; indexParO++)
					{
						Pattern pattern = Pattern.compile(s.name, Pattern.CASE_INSENSITIVE); 
				    	 Matcher matcher = pattern.matcher(WSOP.getCells().get(indexParO, 0).getValue().toString());
				    	  matchFound = matcher.find();
				    	 if(matchFound)
				    		s.OutP.add(WSOP.getCells().get(indexParO, 0).getValue().toString()) ;
						
					}
					
					s.CI=s.InP.size();
					s.CO=s.OutP.size();
					ServiceList.add(s);
					System.out.println("Sname "+s.name);
					System.out.println("size CI CO "+s.CI +" "+s.CO);
					System.out.println("=============================================");
					
				}	

				
				System.out.println("Count of services "+ServiceList.size()+"=============================================");
				
			return ServiceList; 
		  }
		//===================================================================================================================
		  
		  private static  List<service> Sync_Matched_Ser(List<service> SL,int ci,int co,List<String> user_ip_p_l, List<String> user_op_p_l)
		  
		  {
			  List<service> MatchServ = SL.stream().filter(a -> a.CI==ci).filter(a -> a.CO==co).collect(Collectors.toList());
			  return MatchServ;
		  }
		  //==================================================================================================================================
		  
		  
		  
		  
		  private static List<service> Evaluater(List<service> SL,List<String> user_ip_p_l, List<String> user_op_p_l)
		  {
			  
			  int j=0;
			  List<service> suggested=new ArrayList();
			  for(int i=0; i<SL.size(); i++)
				{
				  int ppp2=0;int ppp3=0;
				  boolean matchFound=false,matchFound2=false;
				  int cii=0,coo=0;
				  String serdes= SL.get(i).description;
					// Cal Input similairty
					for(int ip=0; ip< SL.get(i).CI; ip++)
					{
						
				String user_ip_p=user_ip_p_l.get(ip);
				  user_ip_p=user_ip_p.toUpperCase();			 
				  
				   Pattern pattern = Pattern.compile(user_ip_p, Pattern.CASE_INSENSITIVE); 
		    	      Matcher matcher = pattern.matcher(serdes);
		    	    
		    	     matchFound = matcher.find();
		    	      if(matchFound)		    	      
		    	    	  cii+=1 ;	  
					
				}
					
					for(int op=0; op< SL.get(i).CI; op++)
					{
				
				String user_op_p=user_op_p_l.get(op);
				
				  user_op_p=user_op_p.toUpperCase();			 
				  
				   Pattern pattern = Pattern.compile(user_op_p, Pattern.CASE_INSENSITIVE); 
		    	      Matcher matcher = pattern.matcher(serdes);
		    	    
		    	     matchFound2 = matcher.find();
		    	      if(matchFound2)		    	      
		    	    	  coo+=1 ;	  
					
				}
					if(cii>=1 || coo>=1)
					{
					suggested.add(SL.get(i));
					
						
			    		System.out.println((++j)+". "+SL.get(i).name + "    "+SL.get(i).description+"  "+cii+"   "+coo);
					}
					
				}
			  
			  return suggested;
			  
		  }
		  //====================================================================================================================
		  
		  
		  private static void FindDoM(List<service> SL,int ci,int co,List<String> user_ip_p_l, List<String> user_op_p_l,double th)
		  {
			
				double inputSim=0,outputSim=0,totSim=0;
				List<service> MatchServ = SL.stream().filter(a -> a.CI==ci).filter(a -> a.CO==co).collect(Collectors.toList());
				List<RecomService> FinalMatchServ=new ArrayList();
				boolean matchFound=false;
				System.out.println("Count match ser  "+MatchServ.size());
				for(int i=0; i<MatchServ.size(); i++)
				{
				  int ppp2=0;int ppp3=0;
					// Cal Input similairty
					for(int ip=0; ip< MatchServ.get(i).CI; ip++)
				    {   
						inputSim=0;outputSim=0;totSim=0;
						String p= MatchServ.get(i).InP.get(ip);
						String user_ip_p=user_ip_p_l.get(ip);
						  user_ip_p=user_ip_p.toUpperCase();
						  int pos=p.indexOf('#');
		    	    	  p=p.substring(pos+2);
						//System.out.println("parm  "+p);
				        Pattern pattern = Pattern.compile(user_ip_p, Pattern.CASE_INSENSITIVE); 
		    	      Matcher matcher = pattern.matcher(p);
		    	     ppp2=p.indexOf(user_ip_p.toUpperCase());  //avaiable(p) subclass of required(user_op_p) direct subsume=.7
	    	         ppp3=user_ip_p.toUpperCase().indexOf(p.toUpperCase());// user_op_p subclass of p directplogin=.9
		    	     matchFound = matcher.find();
		    	      if(matchFound && ppp2==0)
		    	      {
		    		 inputSim+=1;
		    	      }
		    	      
		    	     // else if(matchFound && ppp2>0)  //query direct plugin of something we have
		    	    	//  outputSim+=.7;
		    	      //else if (ppp3>0)
		    	    	//  outputSim+=.9;
		    	      
		    	      else
		    	      {
		    	    	
		    	    	//  System.out.println("new  p "+p);
		    	    	  inputSim+=compute(user_ip_p,p);
		    	      }
		    	      
				   }// inner loop
					
				inputSim=inputSim/MatchServ.get(i).CI;	
				
							
				
				
				//Cal output sim
				for(int io=0; io< MatchServ.get(i).CO; io++)
				 {
					
					//System.out.println("parmout count  "+io); 
					int p2=0,p3=0;
					String p= MatchServ.get(i).OutP.get(io);
					
					 int pos=p.indexOf('#');
	    	    	  p=p.substring(pos+2);
	    	    	String  user_op_p=user_op_p_l.get(io);
	    	    	  user_op_p=user_op_p.toUpperCase();
	    	    	  //System.out.print("service parm  "+p);
					//System.out.println("  user para  "+ user_op_p);
			        Pattern pattern = Pattern.compile(user_op_p, Pattern.CASE_INSENSITIVE); 
	    	        Matcher matcher = pattern.matcher(p);
	    	         p2=p.indexOf(user_op_p.toUpperCase());  //avaiable(p) subclass of required(user_op_p) direct subsume=.7
	    	         p3=user_op_p.toUpperCase().indexOf(p.toUpperCase());// user_op_p subclass of p directplogin=.9
	    	         //System.out.println("************* "+p2);
	    	         
	    	        matchFound = matcher.find();
	    	      if(matchFound && p2==0)  
	    	        {
	    		    outputSim+=1;
	    	        }
	    	      //else if(matchFound && p2>0)  //query direct plugin of something we have
	    	    	  //outputSim+=.7;
	    	      //else if (p3>0)
	    	    	//  outputSim+=.9;
	    	      else
	    	      {
	    	    	 // int pos=p.indexOf('#');
	    	    	 // p=p.substring(pos+2);
	    	    	  //System.out.println("new  p "+p);
	    	    	  outputSim+=compute(user_op_p,p);
	    	      }
	    	    	 
	    	    	  
	    	      
			   }
				outputSim=outputSim/MatchServ.get(i).CO;	
			
				totSim=0.5 * (inputSim+outputSim);
				
				if(totSim>=th)
				   FinalMatchServ.add(new  RecomService(MatchServ.get(i),totSim));
				
			}//outer loop
				
			    FinalMatchServ.sort(Comparator.comparingDouble(RecomService::simV).reversed());
			    
				System.out.println("Sematically  Match Services (SMS) ");
				System.out.println("Number of SMS "+ FinalMatchServ.size())	;	
				for(int i=0;i< FinalMatchServ.size();i++) {
				int pp= FinalMatchServ.get(i).s.name.indexOf('O');
				 FinalMatchServ.get(i).s.name=FinalMatchServ.get(i).s.name.substring(pp+9);
				System.out.print(i+1+"."+ FinalMatchServ.get(i).s.name+"\t\t\t\t\t");	
				System.out.println(String.format("%.2f", FinalMatchServ.get(i).sim)) ;	
				}
		    }//fuc 
		  
		  
		  
		  
		  
		  
		  
		  //============================================================================================================
		    public static void main(String[] args) throws Exception {
		    	List<service> sl= Extract_Services("registery.xls"); //extract syntically matched services
		    	//"title","comedyfilm"
		    	List<String>uipl =new ArrayList<String>();
		    	List<String>uopl =new ArrayList<String>();
		   /* Q1 uipl.add("title");
		    	uopl.add("film");*/
		    	
		   // Q2 	//uipl.add("sport");
		    	//uopl.add("destination");
		    	
		    	
		    	
		    /*Q3	uipl.add("automobile");
		    	uopl.add("price");*/
		    	
		    	
		    	
		    	
		    //Q4	//uipl.add("retailstore");
		    	//uopl.add("food");
		    	//uopl.add("quality");
		    	
		    	
		   /* Q5 	uipl.add("isbn");
		    	uopl.add("book");
		    	uopl.add("author");*/
		    	
		    	
		    	
		 
		    	
		    	
		  /* Q6	 uipl.add("researcher");
		    	uopl.add("address"); */
		    	
		    	
		    	
		  
		    	/* Q7	uipl.add("missile"); 
			    	uopl.add("financing"); */
		    	
		    	
		 /*   Q8 	uipl.add("care");
		    	uopl.add("diagnosticprocess");
		    	uopl.add("time");  */
		    	
		    	/* Q9
		    	 	uipl.add("award");
		    	uipl.add("scholarship");
		      */
		    	
		    	
		    	
		    	/* Q10
	    	 	uipl.add("country");
	    	uipl.add("weather");
	      */
	    	
	    	
		    	
		    	
		    	
		    	List<service> EvaluateList=Sync_Matched_Ser(sl, uipl.size(),uopl.size(),uipl,uopl);
		    	
		    	
		    	List<service> sug=Evaluater(EvaluateList,uipl,uopl);
		    	
		    	FindDoM(sl, uipl.size(),uopl.size(),uipl,uopl,.8);
		    	
		    	
		  }
		    
	}
	
	
		

