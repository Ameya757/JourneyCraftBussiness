import React from "react";
import { Link } from "react-router-dom";
import { motion, AnimatePresence } from "framer-motion";
import { useSelector } from "react-redux";
import { useDispatch } from "react-redux";

const fadeInUp = {
  hidden: { opacity: 0, y: -40 },
  visible: {
    opacity: 1,
    y: 0,
    transition: {
      duration: 0.6,
      ease: "easeOut",
    },
  },
};


const LandingPage = () => {
  const {role , username} = useSelector((state) => state.auth);
  return (
    <div className="min-h-screen bg-fixed bg-gradient-to-br from-indigo-100 to-white bg-[url('/src/assets/home.jpg')] bg-cover bg-center">
      {/* Hero Section */}
      <header className="bg-gradient-to-br from-indigo-700/50 backdrop-blur-xs text-white py-20 text-center shadow-lg">
        <AnimatePresence>
          <motion.div
            initial="hidden"
            animate="visible"
            exit="hidden"
            variants={fadeInUp}
          >
            {role ? ( 
              <h1 className="text-5xl font-serif font-extrabold tracking-tight">
              Welcome to JourneyCraft
              <br /> 
              <h2 className="text-5xl font-serif text-gray-800 font-extrabold tracking-tight">
              {username}
              </h2>
            </h1>
            ) : (
              <h1 className="text-5xl font-serif font-extrabold tracking-tight">
              Welcome to JourneyCraft
            </h1>
            )}
            <p className="mt-4 text-xl">
              Discover and register travel businesses effortlessly
            </p>
            <Link to={!role ? "/loginsignup" : role === "RESTAURANT" ? "/restaurant" : "/guide"}> 
            <motion.button
              whileHover={{ scale: 1.05 }}
              whileTap={{ scale: 0.95 }}
              className="mt-8 px-8 py-3 bg-white text-indigo-600 font-semibold rounded-full shadow-lg hover:bg-gray-200 transition duration-300"
            >
              Get Started
            </motion.button>
            </Link>
          </motion.div>
        </AnimatePresence>
      </header>

      {/* Features Section */}
      <section className="py-20 px-6 text-center">
        <motion.div
          className="max-w-6xl mx-auto"
          initial="hidden"
          whileInView="visible"
          viewport={{ once: true }}
          variants={fadeInUp}
        >
          <h2 className="text-4xl font-bold text-gray-800 mb-12">
            Why Join JourneyCraft?
          </h2>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-10">
            {["Guides", "Restaurants", "Travelers"].map((role, idx) => (
              <motion.div
                key={idx}
                className="p-8 bg-white/50 shadow-xl backdrop-blur-xs rounded-2xl border hover:shadow-2xl transition duration-300"
                whileHover={{ scale: 1.03 }}
                initial="hidden"
                whileInView="visible"
                viewport={{ once: true }}
              >
                <h3 className="text-2xl font-serif font-semibold text-indigo-700">
                  For {role}
                </h3>
                <p className="mt-3 text-gray-600">
                  {role === "Guides" &&
                    "Showcase your expertise and connect with travelers."}
                  {role === "Restaurants" &&
                    "Increase visibility and attract more customers."}
                  {role === "Travelers" &&
                    "Find the best guides and restaurants in your destination."}
                </p>
              </motion.div>
            ))}
          </div>
        </motion.div>
      </section>

      {/* Call to Action */}
      {!role && (<section className="bg-gradient-to-br to-indigo-700/50 backdrop-blur-xs text-white py-20 text-center shadow-lg">
        <motion.div
          initial="hidden"
          whileInView="visible"
          viewport={{ once: true }}
          variants={fadeInUp}
        >
          <h2 className="text-4xl font-bold">Join JourneyCraft Today</h2>
          <p className="mt-4 text-xl">
            Sign up now and start your journey with us.
          </p>
          <Link to="/loginsignup">
            <motion.button
              className="mt-8 px-8 py-3 bg-white text-indigo-700 font-semibold rounded-full shadow-md hover:bg-gray-100 transition duration-300"
              whileHover={{ scale: 1.05 }}
              whileTap={{ scale: 0.95 }}
            >
              Register Now
            </motion.button>
          </Link>
        </motion.div>
      </section>)}

      {/* Footer */}
      <footer className="text-center py-6 bg-gray-900 text-gray-400">
        <p>&copy; 2025 JourneyCraft. All rights reserved.</p>
      </footer>
    </div>
  );
};

export default LandingPage;
