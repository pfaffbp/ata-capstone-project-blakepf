
import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import ProfileClient from "../api/profileClient";

/**
 * Logic needed for the view playlist page of the website.
 */
class ProfilePage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['onLoad', 'renderUserProfile'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */
    async mount() {

        this.client = new ProfileClient();

        this.dataStore.addChangeListener(this.renderUserProfile)
        this.onLoad();
    }

    // Render Methods --------------------------------------------------------------------------------------------------

    async renderUserProfile() {
        let nameArea = document.getElementById("fullName");
        let displayName = document.getElementById("displayName");
        let ageArea = document.getElementById("age");
        let bioArea = document.getElementById('bio');
        let animeArea = document.getElementById('favAnime')
        let friendArea = document.getElementById('friendsList')

        const uData = this.dataStore.get("userData");
        if (uData) {
            let items ="";
                    items += `
                   <p> Display Name: ${uData.displayName} </p>                            
                `;
            let age ="";
            age += `
                   <p> Age: ${uData.age} </p>                             
                `;
            let name ="";
            name += `
                   <p> Name: ${uData.fullName} </p>                             
                `;
            let bio ="";
            bio += `
                   <p> Bio: ${uData.bio} </p>                             
                `;
          let animeList =""
            /*    for (let anime of uData){
                  animeList +=`
                  ${anime.favoriteAnime}
                  `;
              }*/
            let friendList =""
            /*    for (let anime of uData){
                  animeList +=`
                  ${anime.favoriteAnime}
                  `;
              }*/


            displayName.innerHTML = items;
            ageArea.innerHTML = age;
            nameArea.innerHTML = name;
            bioArea.innerHTML = bio;
            animeArea.innerHTML = animeList;
            friendArea.innerHTML = friendList;
        } else {
            displayName.innerHTML = "No Item";
            ageArea.innerHTML = "No Item";
            nameArea.innerHTML = "No Item";
            animeArea.innerHTML = "No Item";
            friendArea.innerHTML = "No Item";

        }
    }

    // Event Handlers --------------------------------------------------------------------------------------------------


    async onLoad(){
        let result = await this.client.getUserData(this.errorHandler);
        this.dataStore.set("userData", result);
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
