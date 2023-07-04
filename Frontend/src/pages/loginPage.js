import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import LoginClient from "../api/loginClient";

/**
 * Logic needed for the view playlist page of the website.
 */
class LoginPage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['onLogin', 'toggle'], this);
        this.dataStore = new DataStore();


    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */
    async mount() {
        this.client = new LoginClient();

        document.getElementById('LoginUser').addEventListener('click', this.onLogin);
        document.getElementById('eyes').addEventListener('click', this.toggle);
        // await this.alreadyLoggedIn();


    }

    // Render Methods --------------------------------------------------------------------------------------------------


    async onLogin(event) {
        event.preventDefault();
        const loginInput = document.getElementById("email-Login").value;
        const loginPassInput = document.getElementById("loginPassword").value;

        try {
            const validEmail = await this.validEmailFormat(loginInput);
            const login = await this.client.getLogin(validEmail, loginPassInput);

            if (login) {
                const displayName = await this.client.getUserDisplayName(loginInput);
                console.log("this is the display name: " + displayName);
                localStorage.setItem("displayName", displayName);
                localStorage.setItem("LoggedIn", loginInput);
                 window.location.href = "homepage.html";
            } else {
                this.showMessage("incorrect email or password!");
            }
        } catch (error) {
            console.error(error);
            this.errorHandler("Error Logging in! Try again...");
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
    const loginPage = new LoginPage();
    loginPage.mount();
};

window.addEventListener('DOMContentLoaded', main);