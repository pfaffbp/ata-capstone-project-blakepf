
import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import ProfileClient from "../api/profileClient";

/**
 * Logic needed for the view playlist page of the website.
 */
class ProfilePage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['onLoad', 'renderUserProfile', 'getNotifications'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */
    async mount() {

        this.client = new ProfileClient();
        document.getElementById('logout').addEventListener('click', this.Logout);
        document.getElementById('updateProfileBtn').addEventListener('click', this.redirectToUpdateProfile);
        this.dataStore.addChangeListener(this.renderUserProfile)
        this.onLoad();
    }

    // Render Methods --------------------------------------------------------------------------------------------------

    async renderUserProfile() {
        let user = localStorage.getItem('displayName')

        if (user != null) {
            document.getElementById("bell").classList.remove("hide");
        }
        
        let nameArea = document.getElementById("fullName");
        let displayName = document.getElementById("displayName");
        let ageArea = document.getElementById("age");
        let bioArea = document.getElementById('bio');
        let animeArea = document.getElementById('favAnime')
        let followersArea = document.getElementById('followers');
        let followingArea = document.getElementById('following');
        let LoggedInArea = document.getElementById('userLoggedIn');


        const uData = this.dataStore.get("userData");
        if (uData) {
            let items ="";
                    items += `
                   ${uData.displayName}                         
                `;
            let age ="";
            age += `
                    Age: ${uData.age}                        
                `;
            let name ="";
            name += `
                   Name: ${uData.fullName}                          
                `;
            let bio ="";
            bio += `
                    Bio: ${uData.bio}                         
                `;
          let animeList =""
            animeList +=`
                 Name: ${uData.favoriteAnime}
                  `;

            let followersList =""
            followersList +=`
              ${uData.followers}
                
           `;
            let followingList =""
            followingList +=`
              ${uData.following}
                
           `;

            displayName.innerHTML = items;
            ageArea.innerHTML = age;
            nameArea.innerHTML = name;
            bioArea.innerHTML = bio;
            animeArea.innerHTML = animeList;
            followersArea.innerHTML = followersList;
            followingArea.innerHTML = followingList;
            LoggedInArea.innerHTML =  user;

        } else {
            displayName.innerHTML = "Display Name:";
            ageArea.innerHTML = "Age:";
            nameArea.innerHTML = "Name:";
            bioArea.innerHTML = "Bio:";
            animeArea.innerHTML = " ";
            followersArea.innerHTML = " ";
            followingArea.innerHTML = " ";


        }
    }

    // Event Handlers --------------------------------------------------------------------------------------------------


    async onLoad() {
        let result = await this.client.getUserData(this.errorHandler);
        this.dataStore.set("userData", result);

        let user = localStorage.getItem('displayName')
        let LoggedInArea = document.getElementById('userLoggedIn');

        let notification = this.client.getNotifications(user);
        console.log(notification);
        
        if (user != null){

            document.getElementById("bell").classList.remove("hide");
            document.getElementById("bell").addEventListener("click", this.getNotifications);

            LoggedInArea.innerHTML =  user;
        }else
            LoggedInArea.innerHTML = "Login" ;
    }

    async Logout(event){
        event.preventDefault();
        localStorage.clear();
        window.location.href = "login.html";
    }
    async redirectToUpdateProfile() {
        window.location.href = "updateProfile.html";
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
    const profilePage = new ProfilePage();
    profilePage.mount();
};

window.addEventListener('DOMContentLoaded', main);
