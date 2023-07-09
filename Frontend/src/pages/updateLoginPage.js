import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import updateLoginClient from "../api/updateLoginClient";

/**
 * Logic needed for the view playlist page of the website.
 */
class UpdateLoginPage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['onUpdateEmail','toggle', 'onLoad'], this);
        this.dataStore = new DataStore();


    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */
    async mount() {
        this.client = new updateLoginClient();
        document.getElementById('updateEmailButton').addEventListener('click', this.onUpdateEmail);
        document.getElementById('eyes').addEventListener('click', this.toggle);
        document.getElementById('logout').addEventListener('click', this.Logout);
        this.onLoad();
        // await this.alreadyLoggedIn();


    }

    // Render Methods --------------------------------------------------------------------------------------------------


    async onUpdateEmail(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();
        // this.dataStore.set("updateEmail", null);
        const emailInput = document.getElementById("email-entry").value;
        const newEmailInput = document.getElementById("newEmail-entry").value;
        const newEmailConfirmInput = document.getElementById("newEmailConfirm-entry").value;
        const passInput = document.getElementById("password").value;
        const cPassInput = document.getElementById("confirm-password").value;

        try {
            await this.validateEmailInput(newEmailInput, newEmailConfirmInput);
            const validEmail = await this.validEmailFormat(newEmailInput);
            await this.validatePasswordInput(passInput, cPassInput);
            const emailUpdate = await this.client.updateEmailByEmail(emailInput, validEmail, passInput);
            this.showMessage(`Email: ${newEmailInput} updated successfully!`);
            localStorage.clear();
            window.location.href = "login.html";


        } catch (error) {
            console.error(error);
            this.errorHandler("Error Updating Email! Try again...");

        }

    }

    async toggle(event){
        const container = document.querySelector(".container-1"),
            pwShowHide = document.querySelectorAll(".showHidePw"),
            pwFields = document.querySelectorAll(".password");

//   js code to show/hide password and change icon
        pwShowHide.forEach(eyeIcon =>{
            eyeIcon.addEventListener("click", ()=>{
                pwFields.forEach(pwField =>{
                    if(pwField.type ==="password"){
                        pwField.type = "text";

                        pwShowHide.forEach(icon =>{
                            icon.classList.replace("bx-lock-alt", "bx-lock-open-alt");
                        })
                    }else{
                        pwField.type = "password";

                        pwShowHide.forEach(icon =>{
                            icon.classList.replace("bx-lock-open-alt", "bx-lock-alt");
                        })
                    }
                })
            })
        })
    }

    //-------------------checks if passwords match ------------
    async validatePasswordInput(password, confirmPassword) {
        if (!this.validatePassword(password, confirmPassword)) {
            alert("passwords must match");
            throw new Error('Passwords must match.');
        }
    }

    validatePassword(password, newPassword) {
        return password === newPassword;
    }

    //-------------------checks if emails match ------------
    async validateEmailInput(email, confirmEmail) {
        if (!this.validateEmail(email, confirmEmail)) {
            alert("Email must match.");
            throw new Error('Emails must match.');
        }
    }

    validateEmail(email, confirmEmail) {
        return email === confirmEmail;
    }

    //-------------------checks if emails is in valid format ------------
    async validEmailFormat(email) {
        if (!this.checkEmail(email)) {
            alert("invalid email");
            throw new Error('Invalid email address.');
        }else return email;
    }

    checkEmail(email) {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(email);
    }
    async Logout(event){
        event.preventDefault();
        localStorage.clear();
        window.location.href = "login.html";
    }
    async onLoad(){
        let user = localStorage.getItem('displayName')
        let LoggedInArea = document.getElementById('userLoggedIn');
        if (user != null){
            LoggedInArea.innerHTML =  user;
        }else
            LoggedInArea.innerHTML = "Login" ;
    }

}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const updateLoginPage = new UpdateLoginPage();
    updateLoginPage.mount();
};

window.addEventListener('DOMContentLoaded', main);