/**
 * Created by swang on 11/04/2017.
 */
window.onsubmit=validateForm;

function validateForm() {
    var phone1 = parseInt(document.getElementById("phone1").value);
    var phone2 = parseInt(document.getElementById("phone2").value);
    var phone3 = parseInt(document.getElementById("phone3").value);
    var cond1 = document.getElementById("cond1");
    var cond2 = document.getElementById("cond2");
    var cond3 = document.getElementById("cond3");
    var cond4 = document.getElementById("cond4");
    var cond5 = document.getElementById("cond5");
    var period = document.getElementsByName("period");

    var invalidMessages = "";

    if((phone1 < 100 || phone1 > 999) || (phone2 < 100 || phone2 > 999) || (phone3 < 1000 || phone1 > 9999)) {
        invalidMessages += "Invalid phone number.\n";
    }
    if(!cond1.checked && !cond2.checked && !cond3.checked && !cond4.checked && !cond5.checked) {
        invalidMessages += "No Conditions selected.\n";
    }
    if( (cond5.checked) && ((cond1.checked) || (cond2.checked) || (cond3.checked) || (cond4.checked))) {
        invalidMessages += "Invalid conditions selection.\n";
    }

    var idx, flag;
    for(idx = 0; idx < period.length; idx++) {
        if(period[idx].checked) {
            flag = "true";
        }
    }
    if(flag != "true") {
        invalidMessages += "No time period selected.\n";
    }

    invalidMessages += StudyIDValid();

    if(invalidMessages !== "") {
        alert(invalidMessages);
        return false;
    } else {
        var valuesProvided = "Do you want to submit form data?\n";
        if (window.confirm(valuesProvided))
            return true;
        else
            return false;
    }

}

function StudyIDValid() {

    var studyID1 = document.getElementById("studyID1").value;
    var studyID2 = document.getElementById("studyID2").value;

    if(studyID1.length != 4 || studyID2.length != 4) {
        return "Invalid study id.\n";
    } else {
        var substudyID1 = studyID1.substring(1,4);
        var substudyID2 = studyID2.substring(1,4);
        //invalidMessages += studyID1.charAt(0);
        if(studyID1.charAt(0) !== "A" || studyID2.charAt(0) !== 'B' ) {
            return "Invalid study id.\n";
        } else if(isNaN(substudyID1) || isNaN(substudyID2)) {
            return "Invalid study id.\n";
        }
    }
    return "";
}