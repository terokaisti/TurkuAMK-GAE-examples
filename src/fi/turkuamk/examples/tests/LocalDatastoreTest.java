package fi.turkuamk.examples.tests;


import static org.junit.Assert.*;

import java.lang.Character.UnicodeBlock;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.junit.*;

import sun.tools.tree.ArrayAccessExpression;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.appengine.repackaged.com.google.common.collect.Lists;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.apphosting.api.DatastorePb.Query.Filter;
import com.google.code.twig.ObjectDatastore;
import com.google.code.twig.FindCommand.BranchFindCommand;
import com.google.code.twig.FindCommand.MergeOperator;
import com.google.code.twig.FindCommand.RootFindCommand;
import com.google.code.twig.annotation.AnnotationObjectDatastore;
import com.google.code.twig.annotation.Child;
import com.google.code.twig.annotation.Id;
import com.google.code.twig.annotation.Parent;
import com.google.code.twig.util.PredicateToRestrictionAdaptor;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;

import static fi.turkuamk.examples.tests.LocalDatastoreTest.UserEntity.GenderEnum.*;

public class LocalDatastoreTest {
		 private final LocalServiceTestHelper helper =
		        new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
		 private ObjectDatastore ods;
	 
		public static class UserEntity {
			@Id
			public Long id;
			public String firstName;
			public String lastName;
			
			enum GenderEnum { MALE, FEMALE };
			GenderEnum gender;
			
			public UserEntity() {}
			public UserEntity(String firstName, String lastName, GenderEnum g) {
				this.firstName = firstName;
				this.lastName = lastName;
				this.gender = g;
			}
			
		}
		
/* 
        @Test
        public void testStoreLoadEnumSet()
        {
                EnumSet<MyEnum> myEnums = EnumSet.allOf(MyEnum.class);
                EnumContainer container = new EnumContainer();
                container.theEnumSet = myEnums;
                
                ObjectDatastore datastore = new AnnotationObjectDatastore();
                Key key = datastore.store(container);
                
                datastore.disassociateAll();
                
                EnumContainer loaded = datastore.load(key);
                
                Assert.assertTrue(loaded.theEnumSet instanceof EnumSet<?>);
                Assert.assertTrue(loaded.theEnumSet.size() == 2);
        }		
*/		
		 
	    @Before
	    public void setUp() {
	        helper.setUp();
	        ods = new AnnotationObjectDatastore();
	    }

	    @After
	    public void tearDown() {
	        helper.tearDown();
	    }

	    private UserEntity newTestUser() {
    		return new UserEntity("A", "B", MALE);
    	}

	    // run this test twice to prove we're not leaking any state across tests
	    private void doTest() {
	        assertEquals(0, Iterators.size(ods.find().type(UserEntity.class).now()));
	        int i;
	        for( i = 0; i < 10; i++ )
	        	ods.store().instance(newTestUser()).now();
	        assertEquals(i, Iterators.size(ods.find().type(UserEntity.class).now()));
	    }

	    @Test
	    public void testInsert1() {
	        doTest();
	    }

	    @Test
	    public void testInsert2() {
	        doTest();
	    }
	    /*
	    private void test() {
	    	RootFindCommand<HotelIndex> childIndexCommand = 
	            ods.find() 
	                    .type(HotelIndex.class) 
	                    .fetchFirst(CHUNK_SIZE) 
	                    .fetchNextBy(CHUNK_SIZE) 
	                    .fetchNoFields(); 
		    BranchFindCommand<HotelIndex> branch =   
		    childIndexCommand.branch(MergeOperator.OR); 
		    // each map block query is executed in parallel then merged together 
		    for (Block block : blocks) 
		    { 
		            ChildFindCommand<HotelIndex> parallel = branch.addChildCommand() 
		                    .addFilter("blocks", FilterOperator.EQUAL, block.getValue()); 
		    } 
	
		    // filter out non matching hotels before converting them into typesafe instances 

		    ParentsCommand<Hotel> parentsCommand =   
		    childIndexCommand.<Hotel>returnParentsCommand().now(); 
		    Iterator<Hotel> parents =   
		    parentsCommand.restrictEntities(restriction).now();
	    }

  Predicate<Entity> predicate = new Predicate<Entity>()
                {
                        public boolean apply(Entity input)
                        {
                                return input.getKey().getName().equals("Led Zeppelin");
                        }
                };
	    */
		
		/**
		 *	SELECT COUNT(*) FROM UserEntity WHERE 
		 *		(firstName = "A" AND lastName = "A") 
		 * 
		 * @return
		 */
		@Test
	    public void testAnd1() {
			ods.storeAll(getUserEntities());
	    	RootFindCommand<UserEntity> root = 
	    		ods.find().type(UserEntity.class).fetchNoFields().
	    			addFilter("firstName", FilterOperator.EQUAL, "A").
	    			addFilter("lastName", FilterOperator.EQUAL, "A");

	    	assertEquals(1, Iterators.size(root.now()));	    
	    }

		/**
		 *	SELECT COUNT(*) FROM UserEntity WHERE 
		 *		(firstName = "A" AND lastName = "A") OR  
		 *		(firstName = "A" AND lastName = "B") 
		 * 
		 * @return
		 */
		@Test
	    public void testAnd2() {
			ods.storeAll(getUserEntities());
	    	RootFindCommand<UserEntity> root = 
	    		ods.find().type(UserEntity.class).fetchNoFields();

	    	
	    	BranchFindCommand branch = root.branch(MergeOperator.OR);
	    	branch.addChildCommand().
				addFilter("firstName", FilterOperator.EQUAL, "A").
				addFilter("lastName", FilterOperator.EQUAL, "A");
	    	branch.addChildCommand().
				addFilter("firstName", FilterOperator.EQUAL, "A").
				addFilter("lastName", FilterOperator.EQUAL, "B");

	    	assertEquals(2, Iterators.size(root.now()));
	    }
		
		/**
		 *	SELECT COUNT(*) FROM UserEntity WHERE 
		 *		(firstName = "A" OR lastName = "A") 
		 * @return
		 */
		@Test
	    public void testOr1() {
			ods.storeAll(getUserEntities());
			
			ods.disassociateAll();
			
	    	RootFindCommand<UserEntity> root = 
	    		ods.find().type(UserEntity.class);

	    	BranchFindCommand branch = root.branch(MergeOperator.OR);
	    	branch.addChildCommand().
	    			addFilter("firstName", FilterOperator.EQUAL, "A");
	    	branch.addChildCommand().
	    			addFilter("lastName", FilterOperator.EQUAL, "B");
	    	
	    	Iterator<UserEntity> it = root.now();
	    	// remove duplicate entities from iterator
	    	//it = new HashSet<UserEntity>(Arrays.asList(Iterators.toArray(it, UserEntity.class))).iterator();
	    	assertEquals(4, Iterators.size(it));
	    }

		private List<UserEntity> getUserEntities() {
			List<UserEntity> list = 
	    		Arrays.asList(	new UserEntity("A", "A", MALE), //or2 
	    						new UserEntity("A", "B", FEMALE), //or2
	    						new UserEntity("A", "C", MALE),  //or2
	    						new UserEntity("B", "A", FEMALE), 
								new UserEntity("B", "B", MALE), // or2
								new UserEntity("B", "C", MALE), // or2 
	    						new UserEntity("Pasi", "Kuikka", MALE), // or2
	    						new UserEntity("Jorma", "Kuikkaniemi", MALE), // or2 
	    						new UserEntity("Liisa", "Kuikkanen", FEMALE) // 
	    		);
			return list;
		}		

		/**
		 *	SELECT COUNT(*) FROM UserEntity WHERE 
		 *		(firstName = "A" OR lastName = "B" or gender = MALE) 
		 * @return
		 */
		@Test
	    public void testOr2() {
			ods.storeAll(getUserEntities());
			
			ods.disassociateAll();
	    	RootFindCommand<UserEntity> root = 
	    		ods.find().type(UserEntity.class);
	    	BranchFindCommand branch = root.branch(MergeOperator.OR);
	    	branch.addChildCommand().
	    		addFilter("firstName", FilterOperator.EQUAL, "A");
	    	branch.addChildCommand().
	    		addFilter("lastName", FilterOperator.EQUAL, "B");
	    	branch.addChildCommand().
	    		addFilter("gender", FilterOperator.IN, Lists.newArrayList(MALE, FEMALE)); // manual "propertytranslator" to support DS
	
	    	root.addSort("gender", SortDirection.DESCENDING);
	    	Iterator<UserEntity> it = root.now();
	    	while(it.hasNext()) {
	    		UserEntity e = it.next();
	    		System.out.println(e.firstName+" "+e.lastName+" "+e.gender.toString());
	    	}
	    		
	    	
			assertEquals(7, Iterators.size(root.now()));
	    }
		
		private List<UserEntity> getUserEntities1500() {
			ArrayList<UserEntity> list = new ArrayList<UserEntity>();
			for( int i = 0; i < 500; i++ ) {
				list.add(new UserEntity("A", "B", MALE));
				list.add(new UserEntity("A", "C", FEMALE));
				list.add(new UserEntity("B", "D", MALE));
			}
			return list;
		}		

		@Test
	    public void testOr3Dupe1() {
			ods = new AnnotationObjectDatastore();
			ods.storeAll(getUserEntities1500());
			
			ods.disassociateAll();
	    	RootFindCommand<UserEntity> root = 
	    		ods.find().type(UserEntity.class);
	    	BranchFindCommand branch = root.branch(MergeOperator.OR);
	    	branch.addChildCommand().
	    		addFilter("lastName", FilterOperator.EQUAL, "A");
	    	branch.addChildCommand().
	    		addFilter("gender", FilterOperator.EQUAL, MALE.name()); // manual "propertytranslator" to support DS
	    	
			Iterator<UserEntity>it = root.now();
			
			assertEquals(1000, Iterators.size(it));
	    }

		@Test
	    public void testOr3Dupe2() {
			ods = new AnnotationObjectDatastore();
			ods.storeAll(getUserEntities1500());
			
			ods.disassociateAll();
	    	RootFindCommand<UserEntity> root = 
	    		ods.find().type(UserEntity.class);
	    	BranchFindCommand branch = root.branch(MergeOperator.OR);
	    	branch.addChildCommand().
	    		addFilter("lastName", FilterOperator.EQUAL, "A");
	    	branch.addChildCommand().
	    		addFilter("gender", FilterOperator.EQUAL, MALE.name()); // manual "propertytranslator" to support DS
	    	
	    	Iterator<UserEntity> it = root.now();
			
			assertEquals(1000, Iterators.size(it));
	    }

		@Test
	    public void testOr3Dupe3() {
			ods = new AnnotationObjectDatastore();
			ods.storeAll(getUserEntities1500());
			
			ods.disassociateAll();
	    	RootFindCommand<UserEntity> root = 
	    		ods.find().type(UserEntity.class);
	    	BranchFindCommand branch = root.branch(MergeOperator.OR);
	    	branch.addChildCommand().
	    		addFilter("firstName", FilterOperator.EQUAL, "A");
	    	branch.addChildCommand().
	    		addFilter("lastName", FilterOperator.EQUAL, "A");
	    	branch.addChildCommand().
	    		addFilter("gender", FilterOperator.EQUAL, MALE.name()); // manual "propertytranslator" to support DS
	    	
	    	Iterator<UserEntity> it = root.now();
			
			assertEquals(1000, Iterators.size(it));
	    }
		
		
		
		private void dupeCheck(RootFindCommand<UserEntity> root) {
			// remove duplicate entries with hashset
			Predicate<Entity> predicate = new Predicate<Entity>()
			{
				final HashSet<Entity> hs = new HashSet<Entity>();
				public boolean apply(Entity input)
				{
					return hs.add(input);
				}
			};
			PredicateToRestrictionAdaptor<Entity> restriction = new PredicateToRestrictionAdaptor<Entity>(predicate);
			root.restrictEntities(restriction);
		}

		@Test
	    public void testEnumFilterException() {
			ods.storeAll(getUserEntities());
			ods.disassociateAll();
			try {
		    	RootFindCommand<UserEntity> root = 
		    		ods.find().type(UserEntity.class).
		    		addFilter("gender", FilterOperator.EQUAL, UserEntity.GenderEnum.MALE); // throws IllegalArgumentException
		    	root.now();
			}
	    	catch(IllegalArgumentException e) {
	    		fail("Issue41: Enum values not filtered correctly!");
	    	}
	    }
		/**
		 * SELECT COUNT(*) FROM UserEntity WHERE lastName LIKE "%Kuikkan%
		 */
		@Test
	    public void testPrefix1() {
			ods.storeAll(getUserEntities());
			ods.disassociateAll();
		
			RootFindCommand<UserEntity> root = ods.find().type(UserEntity.class).
				addRangeFilter("lastName", "Kuikkan", "Kuikkan"+"\ufffd");
			assertEquals(2, Iterators.size(root.now()));
						
		}
		
		static class P {
			public @Id Long id;
		}
		static class C {
			public @Id Long id;
			@Parent P parent;
		}
		@Test
		public void testChildLoad() {
			P p = new P();
			C c = new C();
			c.parent = p;
			
			Key pkey = ods.store(p);
			Key ckey = ods.store().instance(c).parent(p).now();
			
			assertEquals(c, ods.load(ckey));
		}
}
