# core: Smart Stock System 🛒📦

Welcome to core, a Java-based backend architecture designed to simulate the inventory, delivery, and point-of-sale logistics of a high-volume retail warehouse.

What started as a foundational exercise in Object-Oriented Programming has evolved into a full-fledged retail management simulation. This project bridges the gap between basic data models and complex algorithmic logistics, built to mirror the actual flow of a fast-paced warehouse floor.

    Disclaimer: core is an independent, educational project and is not affiliated with, endorsed by, or associated with Inter IKEA Systems B.V. or any of its subsidiaries. All product and company names are trademarks or registered trademarks of their respective holders.

## 🚀 Why This Project?

The goal of core is to demonstrate a clear progression of software engineering principles. It moves from rigid data storage to dynamic collections, implements custom sorting algorithms from scratch, and utilizes LIFO/FIFO data structures to simulate physical supply chain movement.

Ultimately, this project transforms a standard inventory tracker into an interactive, text-based Tycoon management game.

### 🛠️ Tech Stack

    Language: Java

    Concepts: Object-Oriented Programming (OOP), Polymorphism, Encapsulation

    Data Structures: Dynamic Lists (ArrayList), Stacks (ArrayStack), Queues (ArrayQueue)

    Algorithms: Selection/Insertion Sort, Binary Search

### 🗺️ Project Roadmap & Evolution

##### Phase 1: The Foundation (v1.0) - Completed

    [x] Data Modeling: Built a robust Product class defining core attributes (Name, Price, Stock Level, Aisle Location).

    [x] Encapsulation: Implemented strict setter logic to prevent illegal data states (e.g., negative pricing or stock levels).

    [x] Business Logic: Created retail-specific methods, including an employee discount calculator.

##### Phase 2: Dynamic Scaling & Sorting (v2.0) - Completed

    [x] Dynamic Collections: Transitioned the InventoryManager from static arrays to dynamic ArrayList<Product> structures.

    [x] The Comparable Contract: Implemented the Comparable<Product> interface to establish a natural sorting framework based on price.

    [x] Custom Algorithms: Wrote a custom Selection Sort algorithm to organize inventory without relying on built-in Java sorting utilities.

    [x] Filter Mechanics: Developed methods to return sub-lists of products bounded by minimum and maximum price thresholds.

##### Phase 3: The Logistics Simulation (v3.0) - In Progress

    [ ] Data Structure Constraints: Strictly utilize custom ArrayStack and ArrayQueue classes (avoiding built-in JCF libraries to demonstrate fundamental algorithmic understanding).

    [ ] Delivery Logistics (LIFO): Implement an ArrayStack to simulate the unloading of flat-pack furniture delivery trucks (last loaded at the factory = first unloaded at the dock).

    [ ] Checkout Logistics (FIFO): Implement a multi-line ArrayQueue system to simulate the customer checkout process.

##### Phase 4: The Tycoon Economy Engine (v4.0) - Planned

    [ ] Game Loop: Introduce a continuous while loop with a Scanner interface, allowing the user to manage the warehouse floor in real-time.

    [ ] Revenue Tracking: Implement a global economy that tracks storeBalance as the checkout queues process customer carts.

    [ ] Dynamic Upgrades: Allow users to spend store revenue to open additional checkout queues or upgrade delivery contracts.

    [ ] Randomized Chaos: Utilize Math.random() during the unloading phase to simulate real-world logistics issues (e.g., damaged flat-packs), requiring the player to manage shrink and loss.

###### 👨‍💻 Author

Emmanuel Damptey - Computer Science Student & Software Developer
