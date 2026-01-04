import NewArrivals from "./NewArrivals/NAinPage.jsx";
import Review from "./Review/Page.jsx";
import Sale from "./Sale/Sale.jsx";
import homeimg from "../../assets/image/homeimg.png";
import "./HomePage.css";

export default function HomePage() {
    return (
        <div className="home-page">
            <img className="homeimg" src={homeimg} alt="homeimg" />
            <Sale />
            <NewArrivals />
            <Review />
        </div>
    )
}