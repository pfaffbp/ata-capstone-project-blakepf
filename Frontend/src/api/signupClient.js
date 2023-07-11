import BaseClass from "../util/baseClass";
import axios from 'axios'

/**
 * Client to call the MusicPlaylistService.
 *
 * This could be a great place to explore Mixins. Currently the client is being loaded multiple times on each page,
 * which we could avoid using inheritance or Mixins.
 * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Classes#Mix-ins
 * https://javascript.info/mixins
 */
export default class SignupClient extends BaseClass {

    constructor(props = {}) {
        super();
        const methodsToBind = ['clientLoaded', 'createLogin'];
        this.bindClassMethods(methodsToBind, this);
        this.props = props;
        this.clientLoaded(axios);
    }

    /**
     * Run any functions that are supposed to be called once the client has loaded successfully.
     * @param client The client that has been successfully loaded.
     */
    clientLoaded(client) {
        this.client = client;
        if (this.props.hasOwnProperty("onReady")) {
            this.props.onReady();
        }
    }

    /**
     * Gets the concert for the given ID.
     * @param email
     * @param password
     * @param nickname
     * @param errorCallback (Optional) A function to execute if the call fails.
     * @returns The concert
     */



    async createLogin(email, password, nickname, errorCallback) {
        try {
            const response = await this.client.post(`/login/createLogin`, {
                email: email,
                password: password,
                nickname: nickname
            });
            return response.data;
        } catch (error) {
            console.log("this is the error " + error);
            if (error.response.status === 449) {
                alert("Nickname already exists.")
            } else if (error.response.status === 444) {
                alert("Email already exists.")
            } else
                this.handleError("createLogin", error, errorCallback);
            throw error;
        }
    }

    async getNotifications (displayName, errorCallback) {
        try {
            const response = await this.client.get(`notification/getNotification/${displayName}`)
            return response.data;
        } catch (error) {
            this.handleError('getNotifications', error, errorCallback)
        }
    }






    /**
     * Helper method to log the error and run any error functions.
     * @param method
     * @param error The error received from the server.
     * @param errorCallback (Optional) A function to execute if the call fails.
     */
    handleError(method, error, errorCallback) {
        console.error(method + " failed - " + error);
        if (error.response.data.message !== undefined) {
            console.error(error.response.data.message);
        }
        if (errorCallback) {
            errorCallback(method + " failed - " + error);
        }
    }
}