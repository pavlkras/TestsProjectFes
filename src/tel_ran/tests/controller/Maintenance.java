package tel_ran.tests.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import tel_ran.tests.services.interfaces.ICommonAdminService;
import tel_ran.tests.services.interfaces.IMaintenanceService;

@Controller
@Scope("session")
@RequestMapping({ "/", "/Maintenance" })
public class Maintenance extends AbstractAdminActions {
	// use case 3.3.3 Test Maintenance
	// -------------------- OBJECT CREATION ---------------------------------------------------------- //
	
	public Maintenance(){
		super();
	}
	
	@Autowired
	@Qualifier("maintanenceService")
	public void setObject(ICommonAdminService maintenanceService) {
		adminService = maintenanceService;
	}	
	
	// -------------------- PUBLIC METHODS ------------------------------------------------------------ //

	/**
	 * Start page for Maintenance
	 *  
	 */
	@RequestMapping({ "/Maintenance" ,"/Ma"})
	public String mappingFromIndexPage(Model model) {			
		return "maintenance/MaintenanceSignInPage";
	}
	@RequestMapping({"/aadd_question"})
	public String eddquesrion(Model model) {			
		return "maintenance/MaintenanceAddingPage";
	}
////--------------------- Search in to data base by category witch levelOfDifficulty or free ( returned all questions from DB) -----//
	@RequestMapping(value = "/search_actions" , method = RequestMethod.POST )
	public String searchProcessingPage(String metaCategory,	String levelOfDifficulty, Model model) {
		AutoInformationTextHTML(buildingCategory1CheckBoxTextHTML());
		////
		clearStringBuffer();
		StringBuffer outResultText;
		if(metaCategory != null && !metaCategory.equalsIgnoreCase("")){
			outResultText = new StringBuffer();
			List<String> resultDB = adminService.searchAllQuestionInDataBase(metaCategory, null, null, Integer.parseInt(levelOfDifficulty));			
			////
			if(resultDB.size() > 0){				
				outResultText.append("<table><tr><th>N</th><th>Question</th><th>Category</th></tr>");//<th>Level of difficulty</th>
				////
				for (String questionLine : resultDB) {					
					String[] element = questionLine.split(IMaintenanceService.DELIMITER);	
					outResultText.append("<tr onclick='test(" + element[0] + ")'><td> "+ element[0]+ " </td>");// question id// set id for click event
					outResultText.append("<td> "+ element[1] + " </td>");// question text						
					outResultText.append("<td> "+ element[2] + " </td>");// question text
				}	
				outResultText.append("</table><br>");								
				autoGeneratedHTMLFormText = outResultText;				
			}else{
				autoGeneratedHTMLFormText.append("<p>Selected Category witch this "+levelOfDifficulty+" level is Empty</p>");
			}
		}else{			
			autoGeneratedHTMLFormText.append("<p>No Selected Category</p>");
		}
		////
		return "maintenance/MaintenanceUpdatePage";// return too page after action
	}
	//------- getting question and attributes for change ---------// Begin //
	@RequestMapping({ "/fillFormForUpdateQuestion" })
	public String creationUpdateForm(String questionID, Model model) {	
		/*  indexes in array
		 *          0  QuestionText()                  + DELIMITER// text of question
					1  Description()                   + DELIMITER// text of  Description 	
					2  LineCod()                       + DELIMITER// code question text	
					3  LanguageName()                  + DELIMITER// language of sintax code in question
					4  MetaCategory()                  + DELIMITER// meta category 
					5  Category()                      + DELIMITER// category of question
					6  Id()                            + DELIMITER// static information
					7  CorrectAnswer()                 + DELIMITER// correct answer char 
					8  NumberOfResponsesInThePicture() + DELIMITER// number of answers chars on image					
					9  LevelOfDifficulty()             + DELIMITER// level of difficulty for question*/
		clearStringBuffer();
		try {
			if ((Integer.parseInt(questionID)) > 0) {				
				String[] tempQueryRessult = adminService.getQuestionById(questionID,IMaintenanceService.ACTION_GET_FULL_ARRAY);	
				String[] questionElementsArray = tempQueryRessult[0].split(IMaintenanceService.DELIMITER);
				////  ------- question id and question text
				autoGeneratedHTMLFormText.append("<form name='formTag' action='updateOneQuestion' method='post'>"
						+ "<h2>Question Number: " + questionElementsArray[6] + ".</h2><br>"
						+ "Question text<br><textarea name='questionText' rows='7'>" + questionElementsArray[0] + "</textarea><br>");
				//// ---- description text area
				autoGeneratedHTMLFormText.append("Description text<br><textarea name='descriptionText' rows='7'>" + questionElementsArray[1] + "</textarea><br>");
				//// --- image block
				if(tempQueryRessult[1] != null){
					autoGeneratedHTMLFormText.append("<br><img class='imageQuest' src='"+tempQueryRessult[1]+"' alt='image not support'><br>");	
					autoGeneratedHTMLFormText.append("Image Link<br><textarea name='fileLocationPath'>" + tempQueryRessult[2] + "</textarea><br>");
				}else{
					autoGeneratedHTMLFormText.append("Resurs Full Path <br><textarea name='fileLocationPath'>" + tempQueryRessult[2] + "</textarea><br>");
				}
				//// ---- code question block								
				autoGeneratedHTMLFormText.append("Code Text <br><textarea name='codeText' rows='7'>" + questionElementsArray[2] + "</textarea><br>");						
				//// --- meta category of question
				autoGeneratedHTMLFormText.append("Question Meta Category<br> <input type='text' name='metaCategory' value='"	+ questionElementsArray[4] + "'><br>");
				//// ---- category
				autoGeneratedHTMLFormText.append("Question Category<br> <input type='text' name='category' value='"	+ questionElementsArray[5] + "'><br>");
				// --- adding level check box							
				String check = questionElementsArray[9];
				int checkRes = Integer.parseInt(check);				
				autoGeneratedHTMLFormText.append("Question Level<br>");
				for(int i=1 ; i <= 5 ; i++){
					if (checkRes == i) {
						autoGeneratedHTMLFormText.append("<input checked='checked' type='radio' name='levelOfDifficulty' value='" + i+"'>"+ i);
					} else {
						autoGeneratedHTMLFormText.append("<input type='radio' name='levelOfDifficulty' value='" + i +"'>" + i);
					}
				}			
				////  adding answers list if exist			
				if(tempQueryRessult[3] != null){					
					String[] tempRes = tempQueryRessult[3].split(IMaintenanceService.DELIMITER);

					autoGeneratedHTMLFormText.append("<br> Answers for Question <br>");
					for(int i=0; i < tempRes.length; i++){
						autoGeneratedHTMLFormText.append(IMaintenanceService.ANSWER_CHAR_ARRAY[i] + ". <textarea name='at" + i +"'>"
								+ tempRes[i] + "</textarea><br>");						
					}				
				}else{   
					// if not exist
					autoGeneratedHTMLFormText.append("<p> No Answers For this question</p>");
				}
				////   ---- language of sintax code
				autoGeneratedHTMLFormText.append("<br>Language Name<br>");			
				autoGeneratedHTMLFormText.append("<input type='text' name='languageName' value='" + questionElementsArray[3] + "'><br>");
				//// ----- correct answer char 
				autoGeneratedHTMLFormText.append("<br>Correct Answer<br>");			
				autoGeneratedHTMLFormText.append("<input type='text' name='correctAnswer' value='" + questionElementsArray[7] + "' size='2'><br>");
				//// --- number responses on picture
				autoGeneratedHTMLFormText.append("<br>Number Answers on Pictures<br>");			
				autoGeneratedHTMLFormText.append("<input type='text' name='numAnswersOnPictures' value='" + questionElementsArray[8] + "' size='2'><br>");
				autoGeneratedHTMLFormText.append("<input type='text' name='questionID' value='" + questionElementsArray[6] + "' style='visibility: hidden;'><br>");
				//// ---- button send and change question
				autoGeneratedHTMLFormText.append("<input type='submit' value='Change Question'>");
				autoGeneratedHTMLFormText.append("</form>");
			} else {
				autoGeneratedHTMLFormText.append("Number of Question is Wrong. Input real number of question");
			}
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			System.out.println("catch create form for update question FES");//----------------------------------------------------sysout
			autoGeneratedHTMLFormText.append("Number of Question is Empty. Input number of question");
		}	
		AutoInformationTextHTML(buildingCategory1CheckBoxTextHTML());
		return "maintenance/MaintenanceUpdatePage";
	}
	//------- getting question and attributes for change ---------// END //

	// -------- method delete from DB Tables Question and Answer By ID -----// Begin //
	@RequestMapping({ "/deleteAction" })
	public String deleteFromTablesQuestionAndAnswer(String questionIDdelete, Model model) {
		clearStringBuffer();
		String tempQueryRessult = adminService.deleteQuetionById(questionIDdelete);
		AutoInformationTextHTML(buildingCategory1CheckBoxTextHTML());
		autoGeneratedHTMLFormText.append(tempQueryRessult);// out text to Page
		return "maintenance/MaintenanceUpdatePage";
	}
	

}