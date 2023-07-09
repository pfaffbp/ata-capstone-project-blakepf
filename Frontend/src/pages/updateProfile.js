import ProfileClient from "../api/profileClient";

const profileClient = new ProfileClient();

document.getElementById('updateBtn').addEventListener('click', updateUserProfile);

async function updateUserProfile() {
    const displayName = document.getElementById('displayName').value;
    const age = document.getElementById('age').value;
    const fullName = document.getElementById('fullName').value;
    const bio = document.getElementById('bio').value;

    const user = {
        displayName,
        age,
        fullName,
        bio
    };

    await profileClient.updateUser(user, errorHandler);

    // Redirect to the profile page after the update
    window.location.href = "profilePage.html";
}

function errorHandler(error) {
    // Handle the error as needed
    console.error(error);
}
