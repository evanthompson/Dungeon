This project is a continuation of what I started within demo/collisions.
Since then, I have added:

-	Image processing and display (visual rep of game objects)
-	Smooth(er) player movmement, by dissociating player input
	from triggering view updates. Also, addition of acceleration
-	Better separation of the model, view, and controller to
	more fully utilize the MVC software architecture.
-	Integration of the Observer design pattern.
-	Threading for concurrency.
-	Database Storage (SQLite) to keep track of information between
	sessions (stats, saves, characters).


TODO List:
-Movement
	--Level traversal
		---Connect stairs vertically in pairs (same x/y)
		---Check for collisions traversing floors
		---Modify how player interacts with stair objects
		
	--Floor size
		---How far off screen will the floor run?
		---How will the game deal with screen movement
			Hero always centered, or complete screen swaps?
		
-Battle
	--Contact (direct or weapon)
	--Animation (sword?)
	--Results
		---Loot acquisition

-Images
	--Player
		-- Update of direction (visually)
		-- Walking animation (feet movement)
	--Mobs
	--Floor tiles
	--walls
	
-Game States
	-- Menus
		-- Inventory / stats
		-- Opening / Game over menu
		
Errors that could, or have cropped up:

ERROR_WIDGET_DISPOSED - if the receiver has been disposed 
ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver
