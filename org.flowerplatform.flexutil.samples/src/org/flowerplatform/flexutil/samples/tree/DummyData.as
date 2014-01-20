/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package org.flowerplatform.flexutil.samples.tree {
	import mx.collections.ArrayCollection;

	public class DummyData {
		
		public static function getDummyHierarchy():TreeNode {
			var root:TreeNode = new TreeNode();
			root.name = "root";
			
			var foo:TreeNode = new TreeNode('foo');
			foo.parent = root;
			var bar:TreeNode = new TreeNode('bar');
			bar.parent = root;
			var baz:TreeNode = new TreeNode('baz');
			baz.parent = root;
			var bolts:TreeNode = new TreeNode('bolts');	
			bolts.parent = root;
			root.children = new ArrayCollection([foo,bar,baz,bolts]);
			root.hasChildren = true;
			
			var fred:TreeNode = new TreeNode('fred');
			fred.parent = foo;
			var fade:TreeNode = new TreeNode('fade');
			fade.parent = foo;
			var filthy:TreeNode = new TreeNode('filthy');
			filthy.parent = foo;
			foo.children = new ArrayCollection([fred,fade,filthy]);
			foo.hasChildren = true;
			
			var box:TreeNode = new TreeNode('box');
			box.parent = bar;
			var bushel:TreeNode = new TreeNode('bushel');
			bushel.parent = bar;
			var blossom:TreeNode = new TreeNode('blossom');
			blossom.parent = bar;
			var brotwurst:TreeNode = new TreeNode('brotwurst');
			brotwurst.parent = bar;
			var bundle:TreeNode = new TreeNode('bundle');
			bundle.parent = bar;
			var bombshell:TreeNode = new TreeNode('bombshell');
			bombshell.parent = bar;
			var bloodletting:TreeNode = new TreeNode('bloodletting');
			bloodletting.parent = bar;
			bar.children = new ArrayCollection([box,bushel,blossom,brotwurst,bundle,bombshell,bloodletting]);
			bar.hasChildren = true;
			
			var red:TreeNode = new TreeNode('red');
			red.parent = fred;
			var green:TreeNode = new TreeNode('green');
			green.parent = fred;
			var blue:TreeNode = new TreeNode('blue');
			blue.parent = fred;
			fred.children = new ArrayCollection([red,green,blue]);
			fred.hasChildren = true;
			
			var lily:TreeNode = new TreeNode('lily');
			lily.parent = blossom;
			var lilac:TreeNode = new TreeNode('lilac');
			lilac.parent = blossom;
			var pansy:TreeNode = new TreeNode('pansy');	
			pansy.parent = blossom;
			blossom.children = new ArrayCollection([lily,lilac,pansy]);
			blossom.hasChildren = true;
						
			return root;
		}
	}
}