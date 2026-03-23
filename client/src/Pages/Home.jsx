import BgRemovalSteps from "../Components/BgRemovalSteps"
import BgSlider from "../Components/BgSlider"
import Header from "../Components/Header"
import Pricing from "../Components/Pricing"
import Testimonials from "../Components/Testimonials"
import TryNow from "../Components/TryNow"

const Home = () => {
    return (
        <div className="max-w-7xl mx-auto px-4 sm:px-6 sm:px-6 lg:px-8 py-16 font-['Outfit']">
            {/* Hero Section */}
            <Header />

            {/* Backgrounf Remeval steps section */}
            <BgRemovalSteps />

            {/* Backgrond removal slider section */}
            <BgSlider />

            {/* Buy credits plan section */}
            <Pricing />

            {/* User tesmimonials */}
            <Testimonials />

            {/* Try now */}
            <TryNow />
        </div>
    )
}

export default Home
