import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import SignupClient from "../api/signupClient";
import bcrypt from 'bcryptjs';


/**
 * Logic needed for the view playlist page of the website.
 */
class SignupPage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['onCreateLogin', 'toggle'], this);
        this.dataStore = new DataStore();


    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */
    async mount() {
        this.client = new SignupClient();
        document.getElementById('createUser').addEventListener('click', this.onCreateLogin);
        document.getElementById('eyes').addEventListener('click', this.toggle);



        // await this.alreadyLoggedIn();


    }

    // Render Methods --------------------------------------------------------------------------------------------------


    async onCreateLogin(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();
        this.dataStore.set("createLogin", null);


      const emailInput = document.getElementById("email-entry").value;
      const passInput = document.getElementById("password").value;
      const cPassInput = document.getElementById("confirm-password").value;


        try {

            await this.validateUserInput(passInput, cPassInput);
            const validEmail = await this.validEmailFormat(emailInput)
            const hashedPassword = await bcrypt.hash(passInput, 10);
            const login = await this.client.createLogin(validEmail, hashedPassword);
            this.dataStore.set('login', emailInput)
            this.showMessage(`Login ${emailInput} created successfully!`);
            window.location.href = "login.html";
        } catch (error) {
            console.error(error);
            this.errorHandler("Error creating Login! Try again...");
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
    async validateUserInput(password, confirmPassword) {
        if (!this.validatePassword(password, confirmPassword)) {
            alert("passwords must match");
            throw new Error('Passwords must match and be at least 8 characters long');
        }
    }

    validatePassword(password, confirmPassword) {
        return password.length >= 8 && password === confirmPassword;
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


}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const signupPage = new SignupPage();
    await signupPage.mount();
};

window.addEventListener('DOMContentLoaded', main);