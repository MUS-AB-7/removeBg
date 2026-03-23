import Footer from "./Components/Footer"
import Menubar from "./Components/Menubar"
import Home from "./Pages/Home"
import { Routes, Route } from "react-router-dom"
import { Toaster } from "react-hot-toast"
import UserSyncHandler from "./Components/UserSyncHandler"
import { RedirectToSignIn, SignedIn, SignedOut } from "@clerk/clerk-react"
import Result from "./Pages/Result"
import BuyCredits from "./Pages/BuyCredits"

const App = () => {
  return (
    <div>
      <UserSyncHandler />
      <Menubar />
      <Toaster />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/pricing" element={<BuyCredits />} />
        <Route path="/result" element={
          <>
            <SignedIn>
              <Result />
            </SignedIn>
            <SignedOut>
              <RedirectToSignIn />
            </SignedOut>
          </>
        }
        />
      </Routes>
      <Footer />
    </div>
  )
}

export default App
