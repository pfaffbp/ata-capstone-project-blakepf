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
export default class SearchUsersClient extends BaseClass {

    constructor(props = {}){
        super();
        const methodsToBind = ['clientLoaded', 'getUserData', 'follow', 'unfollow', 'getNotifications', 'setNotification' ];
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
        if (this.props.hasOwnProperty("onReady")){
            this.props.onReady();
        }
    }

    /**
     * Gets the concert for the given ID.
     * @param id Unique identifier for a concert
     * @param errorCallback (Optional) A function to execute if the call fails.
     * @returns The concert
     */
    async getUserData(errorCallback) {

        const displayName = document.getElementById('search-bar').value;

        try {
            const response = await this.client.get(`/user/${displayName}/searchByDisplayName/`);
            return response.data;
        }catch (error){
            this.handleError('getUserData', error, errorCallback)
        }
    }

    async follow (displayName, friendDisplayName, errorCallback) {
        try {
            const response = await this.client.post(`/user/${displayName}/followUser/${friendDisplayName}`)
            return response.data;
        } catch (error) {
            this.handleError('follow', error, errorCallback);
        }
    }

    async unfollow (displayName, friendFullName, errorCallback) {
        try {
            const response = await this.client.delete(`/user/${displayName}/unfollowUser/${friendFullName}`)
            return response.data;
        } catch (error) {
            this.handleError('unfollow', error, errorCallback);
        }
    }

    async getNotifications (displayName, errorCallback) {
        try {
            const response = await this.client.get(`/notification/getNotification/${displayName}`)
            return response.data;
        } catch (error) {
            this.handleError('getNotifications', error, errorCallback)
        }
    }


    async setNotification (displayName, notificationRequest, requester, errorCallback) {
        try {
            const response = await this.client.post(`/notification/setNotification/${displayName}`, {
                requestedUUID: displayName,
                userRequest: {
                    displayName: requester,
                    action: notificationRequest
                },
                hasBeenViewed:  false});
            return response.data;
        } catch (error) {
            this.handleError('setNotifications', error, errorCallback);
        }
    }





    /**
     * Helper method to log the error and run any error functions.
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
