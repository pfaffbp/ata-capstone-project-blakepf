import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import LoginSignupClient from "../api/LoginSignupClient";

/**
 * Logic needed for the view playlist page of the website.
 */
class LoginSignupPage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods([ 'onCreateLogin', 'onLogin'], this);
        this.dataStore = new DataStore();


    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */
    async mount() {
        this.client = new LoginSignupClient();
        document.getElementById('createUser').addEventListener('click', this.onCreateLogin);
        document.getElementById('LoginUser').addEventListener('click', this.onLogin);
       // await this.alreadyLoggedIn();



    }

    // Render Methods --------------------------------------------------------------------------------------------------



    async onCreateLogin(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();
       this.dataStore.set("createLogin", null);
      /*  const form = document.querySelector("form"),
            emailField = form.querySelector(".email-field"),
            emailInput = emailField.querySelector(".email").value,
            passField = form.querySelector(".create-password"),
            passInput = passField.querySelector(".password").value,
            cPassField = form.querySelector(".confirm-password"),
            cPassInput = cPassField.querySelector(".cPassword").value;*/
        const emailInput = document.getElementById('email-entry').value;
        const passInput = document.getElementById('password').value;
        const cPassInput = document.getElementById('confirm-password').value;

     /*   const eyeIcons = document.querySelectorAll(".show-hide");
        eyeIcons.forEach((eyeIcon) => {
            eyeIcon.addEventListener("click", () => {
                const pInput = eyeIcon.parentElement.querySelector("input"); //getting parent element of eye icon and selecting the password input
                if (pInput.type === "password") {
                    eyeIcon.classList.replace("bx-hide", "bx-show");
                    return (pInput.type = "text");
                }
                eyeIcon.classList.replace("bx-show", "bx-hide");
                pInput.type = "password";
            });
        });*/


        try {
            await this.validateUserInput(passInput, cPassInput);
            const login = await this.client.createLogin(emailInput, passInput);
            this.dataStore.set('login', emailInput)
            this.showMessage(`Login ${emailInput} created successfully!`);
            /*console.log('Login', login)*/
           // form.reset();//resets the forum if successful leaves info if not


        }catch (error) {
            console.error(error);
            this.errorHandler("Error creating Login! Try again...");

        }

        }

        async onLogin(event) {
            event.preventDefault();
            this.dataStore.set("LoggedIn", null);
            const loginInput = document.getElementById("email-Login").value;
            const loginPassInput = document.getElementById("loginPassword").value;
            /*const  form = document.querySelector("form"),
                loginField = form.querySelector(".login-email-field"),
                loginInput = loginField.querySelector("email").value,
                passField = form.querySelector("login-password"),
                loginPassInput = passField.querySelector("password").value;*/
            try {
                const login = await this.client.getLogin(loginInput, loginPassInput);
                if (login) {
                    this.showMessage(`Logged ${loginInput} successfully!`);
                    localStorage.setItem("LoggedIn", JSON.stringify(loginInput));
                  //  console.log(localStorage.getItem("LoggedIn"));
                    //window.location.href = "homepage.html";
                } else {
                    this.showMessage("incorrect email or password!");
                }
            } catch (error) {
                console.error(error);
                this.errorHandler("Error Logging in! Try again...");
            }

        }

    async validateUserInput(passInput, cPassInput) {
        if (!this.validatePassword(passInput, cPassInput)) {
            throw new Error('Passwords must match.');
        }
        }

  validateEmail(email, confirmEmail){
        return email === confirmEmail;
  }

    validatePassword(passInput, cPassInput) {
        return passInput === cPassInput;
    }



}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const loginSignupPage = new LoginSignupPage();
     loginSignupPage.mount();
};

window.addEventListener('DOMContentLoaded', main);
