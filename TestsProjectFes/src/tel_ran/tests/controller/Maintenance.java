package tel_ran.tests.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import tel_ran.tests.services.interfaces.IMaintenanceService;

@Controller
@Scope("session")
@RequestMapping({ "/", "/Maintenance" })
public class Maintenance {
	// use case 3.3.3 Test Maintenance
	public Maintenance(){
		AutoGeneratedHTMLFormText = new StringBuffer();
		AutoGeneratedInformationTextHTML = new StringBuffer();
	}
	@Autowired
	IMaintenanceService maintenanceService;
	//
	private static  StringBuffer AutoGeneratedInformationTextHTML;
	private static StringBuffer AutoGeneratedHTMLFormText;
	private String loginForm = "<form class='myButton' action = 'maintenance_login_action'  method = 'get'>"
			+ "Name: <input type='text'  name='username'><br><br>"
			+ "Password: <input type='password'  name='password'><br><br>"
			+ "<input type='submit' class='myButton' value='Administrators Login'></form>";

	/*************************************/
	@RequestMapping({ "/Maintenance" ,"/Ma"})
	public String mappingFromIndexPage(Model model) {
		clearStringBuffer();	
		AutoGeneratedHTMLFormText.append(loginForm);
		return "maintenance/MaintenanceSignInPage";
	}
	/*************************************/
	@RequestMapping({ "/maintenance_login_action" })
	public String authorize(String username, String password, Model model) {
		clearStringBuffer();	
		boolean result = maintenanceService.setAutorization(username,password);// setter flAutorization on Service.
		if (result) {
			AutoGeneratedHTMLFormText.append("<p>Hello "+username+". Glad to see you again</p>"
					+ "<style type='text/css'>#menuButton{display:block;}</style>");
		} else {
			AutoGeneratedHTMLFormText.append(loginForm +"<br><p>User name or Password incorrect!<br> Try again.</p>");
		}
		return "maintenance/MaintenanceSignInPage";// other resurs 
	}
	/**************************************/
	@RequestMapping({ "/maintenanceadd" })
	public String addingPage() {
		clearStringBuffer();
		AutoInformationTextHTML(BuildingCategoryCheckBoxTextHTML());
		return "maintenance/MaintenanceAddingPage";
	}
	/**************************************/
	@RequestMapping({ "/update" })
	public String UpdatePage() {
		clearStringBuffer();
		AutoInformationTextHTML(BuildingCategoryCheckBoxTextHTML());
		return "maintenance/MaintenanceUpdatePage";
	}
	/**************************************/
	@RequestMapping({ "/otherResursCreationMethod" })
	public String MaintenanceOtherResurses() {
		clearStringBuffer();
		AutoGeneratedHTMLFormText.append(BuildingCategoryCheckBoxTextHTML());
		return "maintenance/MaintenanceOtherResurses";
	}	
	/*
	 * 3.3.1. Adding test question Pre-Conditions: 
	 * String questionText,
	 * String fileLocationLink,  
	 * String metaCategory,
	 * String category, 
	 * int levelOfDifficulty,
	 * List<String> answers, 
	 * String correctAnswer,
	 * int questionNumber,
	 * int numberOfResponsesInThePicture,
	 * String description, 
	 * String codeText, 
	 * String languageName
	 */
	@RequestMapping(value = "/add_actions" , method = RequestMethod.POST)
	public String AddProcessingPage(String questionIndex, String questionText, String descriptionText, String codeText,
			String  category1, String metaCategory, String category2, String levelOfDifficulty, 
			String fileLocationLink, String correctAnswer, String numberAnswersOnPicture, 
			String at1, String at2, String at3, String at4,  Model model)
	{	
		boolean actionRes = false; // flag work action
		List<String> answers = null;
		if(at1.length() > 0 && at3.length() > 0){			
			answers = new ArrayList<String>();		 
			answers.add(at1);		answers.add(at2);		answers.add(at3);		answers.add(at4);
		}
		//
		try {			
			int questioNumber;
			
			try {
				questioNumber = Integer.parseInt(questionIndex);// question ID number if question already exist in DB
			} catch (Exception e) {
				questioNumber = 0;
			}
			
			int numberOfResponsesInThePicture;
			
			try {
				numberOfResponsesInThePicture = Integer.parseInt(numberAnswersOnPicture);
			} catch (Exception e) {
				numberOfResponsesInThePicture = 0;
			}
			
			
			String repCategory = null;
			if(category2!=null) {
				repCategory = category2.replaceAll(",", "").replaceAll("none", "");
			}
			
			String repMetaCategory = null;
			if(metaCategory!=null)
				repMetaCategory = metaCategory.replaceAll(",", "").replaceAll("none", ""); 
			////

			int lvl = Integer.parseInt(levelOfDifficulty);
			
			actionRes = maintenanceService.CreateNewQuestion(questionText, fileLocationLink, repMetaCategory, category1, lvl, answers, correctAnswer, 
					questioNumber, numberOfResponsesInThePicture, descriptionText, codeText, repCategory);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("maintenance addProcessingPage :method: Exception");//----------------------------------------------------sysout
		}
		// ==========================================
		if (actionRes) {
			// write alternative flow !!!
			model.addAttribute("logedUser", "<p class='outTextInfoAdded'> Question successfully added</p>");// out
			actionRes = false;
		} else {
			// write alternative flow !!!
			model.addAttribute("logedUser",
					"<p class='outTextInfo'> Error adding the question, the question already exists. Try again</p>");// out
		}
		return "user/UserSignIn"; // return too page after action
	}
	//
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// use case 3.3.2 Update Test Question
	//
	@RequestMapping(value =  "/updateOneQuestion" , method = RequestMethod.POST)
	public String UpdateProcessingPage(String questionID, String questionText, String descriptionText,
			String metaCategory, String category, String levelOfDifficulty, String at0, String at1,
			String at2, String at3, String correctAnswer, String QuTextNumber,
			String fileLocationPath, String codeText, String languageName, String numAnswersOnPictures, Model model) {
		//
		clearStringBuffer();
		////
		List<String> answers = null;
		if(at1 != null){
			if(at0.length() > 1 && at2.length() > 1){
				answers = new ArrayList<String>();			answers.add(at0);			answers.add(at1);			answers.add(at2);			answers.add(at3);
			}
		}
		// ----------------- !!!! TO DO Actions for uppload new image and convert to Base64 and refresh old Base64 file this !!!!!

		boolean result = maintenanceService.UpdateTextQuestionInDataBase(questionID, 
				questionText, descriptionText, codeText, languageName, metaCategory, category,
				Integer.parseInt(levelOfDifficulty), answers, correctAnswer, fileLocationPath, numAnswersOnPictures);	
		String outRes = "";
		if (result) {
			outRes = "<p>Changed Question successfully added</p>";
		} else {
			outRes = "<p>Error Question no changed!!!</p>";
		}
		AutoGeneratedHTMLFormText.append(outRes);// text on page for testing
		AutoInformationTextHTML(BuildingCategoryCheckBoxTextHTML());
		return "maintenance/MaintenanceUpdatePage";// return too page after action
	}
	////--------------------- Search in to data base by category witch levelOfDifficulty or free ( returned all questions from DB) -----//
	@RequestMapping(value = "/search_actions" , method = RequestMethod.POST )
	public String SearchProcessingPage(String metaCategory,	String levelOfDifficulty, Model model) {
		AutoInformationTextHTML(BuildingCategoryCheckBoxTextHTML());
		////
		clearStringBuffer();
		StringBuffer outResultText;
		if(metaCategory != null && !metaCategory.equalsIgnoreCase("")){
			outResultText = new StringBuffer();
			List<String> resultDB = maintenanceService.SearchAllQuestionInDataBase(metaCategory, null, null, Integer.parseInt(levelOfDifficulty));			
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
				AutoGeneratedHTMLFormText = outResultText;				
			}else{
				AutoGeneratedHTMLFormText.append("<p>Selected Category witch this "+levelOfDifficulty+" level is Empty</p>");
			}
		}else{			
			AutoGeneratedHTMLFormText.append("<p>No Selected Category</p>");
		}
		////
		return "maintenance/MaintenanceUpdatePage";// return too page after action
	}
	//------- getting question and attributes for change ---------// Begin //
	@RequestMapping({ "/fillFormForUpdateQuestion" })
	public String CreationUpdateForm(String questionID, Model model) {	
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
				String[] tempQueryRessult = maintenanceService.getQuestionById(questionID,IMaintenanceService.ACTION_GET_FULL_ARRAY);	
				String[] questionElementsArray = tempQueryRessult[0].split(IMaintenanceService.DELIMITER);
				////  ------- question id and question text
				AutoGeneratedHTMLFormText.append("<form name='formTag' action='updateOneQuestion' method='post'>"
						+ "<h2>Question Number: " + questionElementsArray[6] + ".</h2><br>"
						+ "Question text<br><textarea name='questionText' rows='7'>" + questionElementsArray[0] + "</textarea><br>");
				//// ---- description text area
				AutoGeneratedHTMLFormText.append("Description text<br><textarea name='descriptionText' rows='7'>" + questionElementsArray[1] + "</textarea><br>");
				//// --- image block
				if(tempQueryRessult[1] != null){
					AutoGeneratedHTMLFormText.append("<br><img class='imageQuest' src='"+tempQueryRessult[1]+"' alt='image not support'><br>");	
					AutoGeneratedHTMLFormText.append("Image Link<br><textarea name='fileLocationPath'>" + tempQueryRessult[2] + "</textarea><br>");
				}else{
					AutoGeneratedHTMLFormText.append("Resurs Full Path <br><textarea name='fileLocationPath'>" + tempQueryRessult[2] + "</textarea><br>");
				}
				//// ---- code question block								
				AutoGeneratedHTMLFormText.append("Code Text <br><textarea name='codeText' rows='7'>" + questionElementsArray[2] + "</textarea><br>");						
				//// --- meta category of question
				AutoGeneratedHTMLFormText.append("Question Meta Category<br> <input type='text' name='metaCategory' value='"	+ questionElementsArray[4] + "'><br>");
				//// ---- category
				AutoGeneratedHTMLFormText.append("Question Category<br> <input type='text' name='category' value='"	+ questionElementsArray[5] + "'><br>");
				// --- adding level check box							
				String check = questionElementsArray[9];
				int checkRes = Integer.parseInt(check);				
				AutoGeneratedHTMLFormText.append("Question Level<br>");
				for(int i=1 ; i <= 5 ; i++){
					if (checkRes == i) {
						AutoGeneratedHTMLFormText.append("<input checked='checked' type='radio' name='levelOfDifficulty' value='" + i+"'>"+ i);
					} else {
						AutoGeneratedHTMLFormText.append("<input type='radio' name='levelOfDifficulty' value='" + i +"'>" + i);
					}
				}			
				////  adding answers list if exist			
				if(tempQueryRessult[3] != null){					
					String[] tempRes = tempQueryRessult[3].split(IMaintenanceService.DELIMITER);

					AutoGeneratedHTMLFormText.append("<br> Answers for Question <br>");
					for(int i=0; i < tempRes.length; i++){
						AutoGeneratedHTMLFormText.append(IMaintenanceService.ANSWER_CHAR_ARRAY[i] + ". <textarea name='at" + i +"'>"
								+ tempRes[i] + "</textarea><br>");						
					}				
				}else{   
					// if not exist
					AutoGeneratedHTMLFormText.append("<p> No Answers For this question</p>");
				}
				////   ---- language of sintax code
				AutoGeneratedHTMLFormText.append("<br>Language Name<br>");			
				AutoGeneratedHTMLFormText.append("<input type='text' name='languageName' value='" + questionElementsArray[3] + "'><br>");
				//// ----- correct answer char 
				AutoGeneratedHTMLFormText.append("<br>Correct Answer<br>");			
				AutoGeneratedHTMLFormText.append("<input type='text' name='correctAnswer' value='" + questionElementsArray[7] + "' size='2'><br>");
				//// --- number responses on picture
				AutoGeneratedHTMLFormText.append("<br>Number Answers on Pictures<br>");			
				AutoGeneratedHTMLFormText.append("<input type='text' name='numAnswersOnPictures' value='" + questionElementsArray[8] + "' size='2'><br>");
				AutoGeneratedHTMLFormText.append("<input type='text' name='questionID' value='" + questionElementsArray[6] + "' style='visibility: hidden;'><br>");
				//// ---- button send and change question
				AutoGeneratedHTMLFormText.append("<input type='submit' value='Change Question'>");
				AutoGeneratedHTMLFormText.append("</form>");
			} else {
				AutoGeneratedHTMLFormText.append("Number of Question is Wrong. Input real number of question");
			}
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			System.out.println("catch create form for update question FES");//----------------------------------------------------sysout
			AutoGeneratedHTMLFormText.append("Number of Question is Empty. Input number of question");
		}	
		AutoInformationTextHTML(BuildingCategoryCheckBoxTextHTML());
		return "maintenance/MaintenanceUpdatePage";
	}
	//------- getting question and attributes for change ---------// END //

	// -------- method delete from DB Tables Question and Answer By ID -----// Begin //
	@RequestMapping({ "/deleteAction" })
	public String deleteFromTablesQuestionAndAnswer(String questionIDdelete, Model model) {
		clearStringBuffer();
		String tempQueryRessult = maintenanceService.deleteQuetionById(questionIDdelete);
		AutoInformationTextHTML(BuildingCategoryCheckBoxTextHTML());
		AutoGeneratedHTMLFormText.append(tempQueryRessult);// out text to Page
		return "maintenance/MaintenanceUpdatePage";
	}
	// -------- method delete from DB Tables Question and Answer By ID -----// END //
	////-------- USE CASE 3.3.3 Bulk entering test data
	// --------- adding questions from txt file on user computer -----//  Begin  //	
//	@RequestMapping({ "/add_from_file_actions" })
//	public String addFromFileProcessingPage(String textfromfile, Model model) {
//		//sample for text in file question(one line!!! open text file on project - TestsProjectBes root directory )
//		//questionText----imageLink----category----levelOfDifficulti----answer1----answer2----answer3----answer4----correctAnswerChar----questionIndexNumber@end_line@ // as delimiter .00.0
//
//		if(textfromfile != null && textfromfile.length() > 12){	
//			List<String> listTmp = new ArrayList<String>();
//			String[] splitedLinkArray = textfromfile.split(IMaintenanceService.IMAGE_DELIMITER);	
//			//// --- converting from array to list<string>
//			for(int i = 0;i < splitedLinkArray.length;i++){				
//				listTmp.add(splitedLinkArray[i]);// converting from array to list<string>
//			}
//			boolean actionRes = false;
//			//// -- sending converted list<string> to Data Base
//			try{		
//				actionRes = maintenanceService.FillDataBaseFromTextResource(listTmp);	// adding case on BES			
//			} catch (Exception e) {
//				//e.printStackTrace();
//				System.out.println("catch of creating from file questions in FES");//----------------------------------------------------sysout
//			}
//			////
//			if(actionRes){
//				AutoInformationTextHTML("<br><p class='informationTextP'>Adding Questions is - " + actionRes + "</p>");		
//			}else{
//				AutoInformationTextHTML("<br><p class='informationTextP'>Adding Questions is - " + actionRes + "</p>");	// out text to Page
//			}
//		}
//		return "maintenance/MaintenanceOtherResurses";// return too page after action
//	}
	// --------- adding questions from any.txt file on user computer -----//  END  //
	// -------------- Module For Building Questions in to DB ----------------////
	@RequestMapping({ "/moduleForBuildingQuestions" })
	public String ModuleForBuildingQuestions(String category, String nQuestions, String levelOfDifficulty, Model model) {		
		int nQuest = Integer.parseInt(nQuestions);
		boolean actionRes = false;
		try {
			int differentLevel = Integer.parseInt(levelOfDifficulty);
			actionRes = maintenanceService.ModuleForBuildingQuestions(category, differentLevel , nQuest);
		} catch (Exception e) {
			System.out.println("catch call maintenanceaction from FES moduleForBuildingQuestions");//----------------------------------------------------sysout
			//e.printStackTrace();
		}
		//
		if(actionRes){				
			AutoInformationTextHTML("<br><p class='informationTextP'>your request: to build |" + nQuestions	+ "| questions made</p>");		
		}else{			
			AutoInformationTextHTML("<br><p class='informationTextP'>your request: to build |" + nQuestions	+ "| questions failed</p>");
		}
		return "maintenance/MaintenanceOtherResurses";// return too page after action
	}
	////////////////////////////////////////// PRIVATE METHODS FOR THIS CLASS /////////////////////////////////////////////////////
	// ---------------------- Building html text of checkBoxe Category form on Page --------------//
	private String BuildingCategoryCheckBoxTextHTML() {		
		StringBuffer checkedFlyButtons = new StringBuffer();
		try {
			List<String> categoryList = maintenanceService.getAllCategories2FromDataBase();
			if (categoryList != null) {			
				for (String tresR : categoryList) {					
					checkedFlyButtons.append("<option value='" + tresR + "'> "+ tresR + "</option>");					
				}				
			} else {
				checkedFlyButtons.append("<b>No Categories in Data Base</b>");
			}
		} catch (Exception e) {
			//e.printStackTrace();
			checkedFlyButtons.append("<b>No Data Base</b>");
			System.out.println("no data base found");//----------------------------------------------------sysout
		}
		return checkedFlyButtons.toString();// return too page after action
	}
	////
	private void AutoInformationTextHTML(String string) {
		AutoGeneratedInformationTextHTML.append(string);
	}
	// ---------------------- formating data in to string buffer  --------------// 
	private void clearStringBuffer() {
		AutoGeneratedHTMLFormText.delete(0, AutoGeneratedHTMLFormText.length());// clear stringbuffer		
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// This methods work as getter for text to jsp page, in text HTML for sam page
	public static String AutoGeneratedHTMLForm(){		
		String outTextResult = "";
		if(AutoGeneratedHTMLFormText != null){
			outTextResult = AutoGeneratedHTMLFormText.toString();
		}else{
			outTextResult = "<h3>404 Page Not Found </h3>";		
		}			
		return outTextResult;		
	}	
	////
	public static String AutoGeneratedInformationTextHTML(){	
		String outTextResult = "";
		if(AutoGeneratedInformationTextHTML != null){
			outTextResult = AutoGeneratedInformationTextHTML.toString();	
			AutoGeneratedInformationTextHTML.delete(0, AutoGeneratedInformationTextHTML.length());// clear stringbuffer
		}	
		return outTextResult;		
	}
	//// AJAX for upload picture for new question on creating
	@RequestMapping(value="/upload-file", method=RequestMethod.POST)
	public @ResponseBody JsonResponse UploadFile(HttpServletRequest request) {	
		JsonResponse res = new JsonResponse(); 
		String concatRes = " ";

		// TO DO Method !!!!
		res.setStatus("SUCCESS");
		res.setResult(concatRes);
		return res;
	}
	//// ajax meta category question creation onload page action 
	@RequestMapping(value="/categoryCreationAction", method=RequestMethod.POST)  
	public @ResponseBody JsonResponse HandlerCode(HttpServletRequest request) {	
		JsonResponse res = new JsonResponse(); 
		String concatRes = " ";
		List<String> result = maintenanceService.GetPossibleMetaCaterories();
		for(String re:result){		
			concatRes += "<option value='" + re + "'>"+ re +"</option>";
		}
		res.setStatus("SUCCESS"); 
		res.setResult(concatRes);
		return res;
	}
	////static resurse class for JSON, Ajax on company add page TO DO Class ?? or not TO DO :)
	class JsonResponse {
		private String status = null;
		private Object result = null;
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public Object getResult() {
			return result;
		}
		public void setResult(Object result) {
			this.result = result;
		}
	}
}