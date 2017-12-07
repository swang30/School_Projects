function validateRegisterForm() {
	var username = document.getElementById("username").value;
    var password = document.getElementById("password").value;

    // Validate username and password length.
    // username length must be within 40
    // password length must be within 15
    var invalidMessages = "";
    if (username.length > 40) {
    	invalidMessages += "User name length exceeds 40 characters.\n"
    }

    if (password.length > 15) {
    	invalidMessages += "Password length exceeds 15 characters.\n"
    }

	return processResult(invalidMessages);
}


function processResult(invalidMessages) {
    if (invalidMessages !== "") {
        alert(invalidMessages);
        return false;
    }
}