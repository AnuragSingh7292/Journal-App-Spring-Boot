navigator.geolocation.getCurrentPosition(async pos => {
    const lat = pos.coords.latitude;
    const lon = pos.coords.longitude;

    try {
        const response = await fetch(`${BASE_PATH}api/weather?lat=${lat}&lon=${lon}`);
        const data = await response.json();

        if (data.error) {
            console.error(data.error);
            return;
        }

        document.querySelector(".weather-header .location-name").textContent = data.location || "Unknown";
        document.querySelector(".weather-details .temperature").textContent = `${data.temperature.toFixed(1)}°C`;
        document.querySelector(".weather-details .weather-condition").textContent = `Feels like: ${data.feelsLike.toFixed(1)}°C`;
        document.querySelector(".weather-footer .weather-stat:nth-child(1) span").textContent = `${data.windSpeed} km/h`;
        document.querySelector(".weather-footer .weather-stat:nth-child(2) span").textContent = `${data.humidity}%`;

    } catch (error) {
        console.error("Failed to fetch weather data:", error);
    }

}, err => {
    console.error("Geolocation error:", err);
}, { enableHighAccuracy: true, timeout: 10000 });
