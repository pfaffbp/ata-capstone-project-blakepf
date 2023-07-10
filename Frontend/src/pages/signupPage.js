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
        this.bindClassMethods(['onCreateLogin', 'toggle', 'onLoad', 'getNotifications'], this);
        this.dataStore = new DataStore();


    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */
    async mount() {
        this.client = new SignupClient();
        document.getElementById('createUser').addEventListener('click', this.onCreateLogin);
        document.getElementById('eyes').addEventListener('click', this.toggle);
        document.getElementById('logout').addEventListener('click', this.Logout);
        this.onLoad();

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
        const nickname = document.getElementById('nickname').value;

        //forces nickname to lower case
        let toLower = nickname;
        let name = toLower.toLowerCase();

        try {

            await this.validateUserInput(passInput, cPassInput);
            const validEmail = await this.validEmailFormat(emailInput)
            const hashedPassword = await bcrypt.hash(passInput, 10);
            const checkedNickName = await this.validateNickname(name)
            const login = await this.client.createLogin(validEmail, hashedPassword, checkedNickName);
            this.dataStore.set('login', emailInput)
            this.showMessage(`Login ${emailInput} created successfully!`);
            window.location.href = "login.html";
        } catch (error) {
            console.error(error);
                this.errorHandler("Error creating Login! Try again...");
            }
        }



    async toggle(event) {
        const container = document.querySelector(".container-1"),
            pwShowHide = document.querySelectorAll(".showHidePw"),
            pwFields = document.querySelectorAll(".password");

//   js code to show/hide password and change icon
        pwShowHide.forEach(eyeIcon => {
            eyeIcon.addEventListener("click", () => {
                pwFields.forEach(pwField => {
                    if (pwField.type === "password") {
                        pwField.type = "text";

                        pwShowHide.forEach(icon => {
                            icon.classList.replace("bx-lock-alt", "bx-lock-open-alt");
                        })
                    } else {
                        pwField.type = "password";

                        pwShowHide.forEach(icon => {
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
            alert("Passwords must match and be at least 8 characters long");
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
        } else return email;
    }

    checkEmail(email) {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(email);
    }

    //-------------------checks nickname Length ------------
    async validateNickname(nickname) {
        if (!this.checkNicknameLength(nickname)) {
            alert("Limit display name to 16 characters or less.")
            throw new Error('Limit display name to 16 characters or less.')
        } else return nickname;
    }

    checkNicknameLength(nickName){
            return nickName.length <= 16;
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

            document.getElementById("bell").classList.remove("hide");
            document.getElementById("bell").addEventListener("click", this.getNotifications);

            LoggedInArea.innerHTML =  user;
        }else
            LoggedInArea.innerHTML = "Login" ;
    }

    async getNotifications() {
        console.log("In getNotifications");                                 // Checks to see if it makes it to this method when clicking the bell.
        let user = localStorage.getItem("displayName");                     // Grabbing the user name. 
        console.log(user);                                                  // Logging the user name to check and see if it pulls the correct one. 

        const response = await this.client.getNotifications(localStorage.getItem("displayName"), this.errorHandler)         // Sends a get request to the Lambda API.
        console.log(response);
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