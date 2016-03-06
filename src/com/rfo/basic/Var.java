/****************************************************************************************************

BASIC! is an implementation of the Basic programming language for
Android devices.

Copyright (C) 2010 - 2016 Paul Laughton

This file is part of BASIC! for Android

    BASIC! is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    BASIC! is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with BASIC!.  If not, see <http://www.gnu.org/licenses/>.

    You may contact the author or current maintainers at http://rfobasic.freeforums.org

    Apache Commons Net
    Copyright 2001-2011 The Apache Software Foundation

    This product includes software developed by
    The Apache Software Foundation (http://www.apache.org/).

*************************************************************************************************/

package com.rfo.basic;

//Log.v(LOGTAG, "Line Buffer  " + ExecutingLineBuffer);

import java.security.InvalidParameterException;
import java.util.ArrayList;

import android.util.Log;

public abstract class Var {

	// ****************************** Type enum *******************************

	public enum Type {									// a variable can be a string or a number
		NOVAR("X", "none", false, false),
		NUM("N", "numeric", true, false),
		STR("S", "string", false, true);

		private final String mCh;
		private final String mStr;
		private final boolean mIsNumeric;
		private final boolean mIsString;
		private Type(String shortForm, String longForm, boolean isNumeric, boolean isString) {
			mCh = shortForm; mStr = longForm;
			mIsNumeric = isNumeric; mIsString = isString;
		}

		public static Type typeOf(char c) {
			switch (c) {
				case 'n': return NUM;
				case 's': return STR;
				default:  return NOVAR;
			}
		}

		// Use this to get standardized error message.
		public Type isNS() {							// allows only NUM or STR
			if (this == NOVAR) { throw new InvalidParameterException("Untyped variable."); }
			return this;
		}

		public String typeNS() {						// allows only NUM or STR
			isNS();
			return mCh;
		}

		public boolean isNumeric() { return mIsNumeric; }
		public boolean isString() { return mIsString; }
		@Override
		public String toString() { return mStr; }
	}

	// ***************************** Table class ******************************
	// A symbol table. Each Var.Table consists of two ArrayLists.
	// One holds only names, so Collections.binarySearch can use native String compares.
	// The other holds Var objects containing everything about the variables in the table.
	// Each Var has a reference to a Var.Val (value of scalar), a Var.ArrayDef, or a Var.FnDef.
	public static class Table {
		public ArrayList<String> mVarNames;				// Each entry has the variable name string
		public ArrayList<Var> mVars;					// All variables of all types.

		public Table() {
			mVarNames = new ArrayList<String>();
			mVars = new ArrayList<Var>();
		}

		// Delegates.

		public void add(int index, String name, Var var) {
			mVarNames.add(index, name);					// create entry in list of variable names
			mVars.add(index, var);						// create corresponding variable list entry
		}

		public void remove(int index) {
			mVarNames.remove(index);					// remove entry from list of variable names
			mVars.remove(index);						// remove corresponding variable list entry
		}
	}

	// ****************************** Val class *******************************
	// An object of this class holds the value of a scalar variable or an array element.

	protected abstract static class Val {
		protected static final String WRONG_TYPE = "Wrong type for this variable.";

		protected Val() { }

		protected abstract boolean isNumeric();
		protected abstract boolean isString();
		protected abstract void val(double val);
		protected abstract void val(String val);
		protected abstract void addval(double val);
		protected abstract void addval(String val);
		protected abstract double nval();
		protected abstract String sval();
	} // class Val

	public static class NumVal extends Val {
		private double mVal;

		public NumVal() { this(0.0); }
		public NumVal(double val) { mVal = val; }

		@Override public boolean isNumeric() { return true; }
		@Override public boolean isString() { return false; }

		@Override public void val(double val) { mVal = val; }
		@Override public void val(String val) { throw new InvalidParameterException(WRONG_TYPE); }

		@Override public void addval(double val) { mVal += val; }
		@Override public void addval(String val) { throw new InvalidParameterException(WRONG_TYPE); }

		@Override public double nval() { return mVal; }
		@Override public String sval() { throw new InvalidParameterException(WRONG_TYPE); }
	} // class NumVal

	public static class StrVal extends Val {
		private String mVal;

		public StrVal() { this(""); }
		public StrVal(String val) { mVal = val; }

		@Override public boolean isNumeric() { return false; }
		@Override public boolean isString() { return true; }

		@Override public void val(double val) { throw new InvalidParameterException(WRONG_TYPE); }
		@Override public void val(String val) { mVal = val; }

		@Override public void addval(double val) { throw new InvalidParameterException(WRONG_TYPE); }
		@Override public void addval(String val) { mVal += val; }

		@Override public double nval() { throw new InvalidParameterException(WRONG_TYPE); }
		@Override public String sval() { return mVal; }
	} // class StrVal

	public static class ArrayVal extends Val {			// refers to an array element by caching its index
		private ArrayDef mArray;						// where the array data is
		private int mIndex;								// the index of an element in the array in mArray

		public ArrayVal(ArrayDef array) {
			mArray = array;
			mIndex = -1;
		}

		@Override public boolean isNumeric() { return (mArray != null) && mArray.isNumeric(); }
		@Override public boolean isString() { return (mArray != null) && mArray.isString(); }

		public void index(int index) { mIndex = index; }					// set the (1D) index

		// Write the array element value.
		@Override public void val(double val) { mArray.val(val, mIndex); }	// numeric array
		@Override public void val(String val) { mArray.val(val, mIndex); }	// string array

		// Add a value to an array element.
		@Override public void addval(double val) { val(nval() + val); }		// numeric array
		@Override public void addval(String val) { val(nval() + val); }		// string array

		// Read the array element value.
		@Override public double nval() { return mArray.nval(mIndex); }		// numeric array
		@Override public String sval() { return mArray.sval(mIndex); }		// string array
	} // class StrVal

	// **************************** ArrayDef class ****************************
	// Array definition: metadata and data

	protected abstract static class ArrayDef {
		protected static final String WRONG_TYPE = "Wrong type for this array.";

		private int[] mDimList;
		private int[] mArraySizes;
		protected int mLength;
		protected boolean mValid;

		public static ArrayDef create(boolean isNumeric, int[] dimList) {
			return (isNumeric) ? new NumArrayDef(dimList) : new StrArrayDef(dimList);
		}

		protected ArrayDef(int[] dimList) {
			// Record the dimensions of a basic array and calculate the total length.
			// Constructor does not allocate space for the array data.

			// This list of sizes is used to quickly calculate the 1D array element offset
			// of a multi-dimension array when the array is referenced.
			int size = dimList.length;
			int[] arraySizes = new int[size];
			int length = 1;
			for (int d = size - 1; d >= 0; --d) {		// for each Dim from last to first
				int dim = dimList[d];					// get the Dim
				if (dim < 1) { throw new InvalidParameterException("ArrayDef dimList"); }
				arraySizes[d] = length;					// insert the previous total in the ArraySizes List
				length *= dim;							// multiply this dimension by the previous size
			}
			mDimList = dimList;
			mArraySizes = arraySizes;
			mLength = length;
			invalidate();								// not yet valid
		}

		protected abstract boolean isNumeric();
		protected abstract boolean isString();

		protected void invalidate() { mValid = false; }	// mark invalid

		public boolean valid() { return mValid; }
		public int length() { return mLength; }
		public int[] dimList() { return mDimList; }
		protected int[] arraySizes() { return mArraySizes; }

		protected abstract void createArray();			// create and initialize empty array

		protected int getIndex(ArrayList<Integer> indexList) {
			int nDims = mDimList.length;
			int nIndices = indexList.size();
			if (nDims != nIndices) {					// insure index count = dim count
				throw new InvalidParameterException(
						"Expected " + nDims +
						" ind" + ((nDims == 1) ? "ex" : "ices") +
						" but found " + nIndices + ":");
			}

			int index = 0;
			for (int i = 0; i < nDims; ++i) {
				int p = indexList.get(i);				// p = index for this call
				int q = mDimList[i];					// q = DIMed value for this index
				int r = mArraySizes[i];					// r = size for this index
				if (p > q) {							// insure index <= DIMed limit
					throw new InvalidParameterException(
							"Index #"+ (i+1) + " (" + p +
							") exceeds dimension (" + q +
							") at:");
				}
				if (p <= 0) {							// insure index >= 1
					throw new InvalidParameterException("Index must be >=1 at:");
				}
				index += (p-1)*r;						// calculate index
			}
			return index;
		}

		// Index the array, put the index in a new ArrayVal.
		public ArrayVal val(ArrayList<Integer> indexList) {
			ArrayVal val = new ArrayVal(this);
			val.index(getIndex(indexList));
			return val;
		}

		// Raw element access: assume 1D, skip index calculation.
		// Also skip index checking. Java will do it, but caller should
		// check so user doesn't have to deal with the error.
		private void wrongType() { throw new InvalidParameterException(WRONG_TYPE); }
		protected void val(double val, int index) { wrongType(); }
		protected void val(String val, int index) { wrongType(); }
		protected double nval(int index) { wrongType(); return 0.0; }	// does not return
		protected String sval(int index) { wrongType(); return ""; }	// does not return

		// Raw array access.
		protected double[] getNumArray() { wrongType(); return null; }	// does not return
		protected String[] getStrArray() { wrongType(); return null; }	// does not return
	} // class ArrayDef

	public static class NumArrayDef extends ArrayDef {
		private double[] mNum;

		protected NumArrayDef(int[] dimList) { super(dimList); }

		@Override public boolean isNumeric() { return true; }
		@Override public boolean isString() { return false; }

		@Override public void invalidate() {			// mark invalid and release all data
			super.invalidate();
			mNum = null;
		}

		@Override public void createArray() {			// create and initialize empty array
			mNum = new double[mLength];					// initialized to 0.0
			mValid = true;
		}

		// Raw access.
		@Override public void val(double val, int index) { mNum[index] = val; }
		@Override public double nval(int index) { return mNum[index]; }
		@Override public double[] getNumArray() { return mNum; }
	} // class NumArrayDef

	public static class StrArrayDef extends ArrayDef {
		private String[] mStr;

		protected StrArrayDef(int[] dimList) { super(dimList); }

		@Override public boolean isNumeric() { return false; }
		@Override public boolean isString() { return true; }

		@Override public void invalidate() {			// mark invalid and release all data
			super.invalidate();
			mStr = null;
		}

		@Override public void createArray() {			// create and initialize empty array
			mStr = new String[mLength];					// initialized to null
			for (int i = 0; i < mLength; ++i) { mStr[i] = ""; }
			mValid = true;
		}

		// Raw access.
		@Override public void val(String val, int index) { mStr[index] = val; }
		@Override public String sval(int index) { return mStr[index]; }
		@Override public String[] getStrArray() { return mStr; }
	} // class StrArrayDef

	// *********************** FunctionParameter class ************************

	public static class FunctionParameter {
		private Var mVar;								// parameter name, type, and value
		private boolean mByRef = false;

		public FunctionParameter(Var var) { mVar = var; }

		public void var(Var var) { mVar = var; }
		public void byReference(boolean byReference) { mByRef = byReference; }

		public Var var() { return mVar; }
		public boolean byReference() { return mByRef; }
	}

	// ***************************** FnDef class ******************************
	// Function Definition: attributes defined by FN.DEF.

	public static class FnDef {
		private int mLine;								// line number of fn.def
		private Var mVar;								// name and type of this function
		private ArrayList<FunctionParameter> mParms;	// list of function parameters

		public FnDef(int line, Var var, ArrayList<FunctionParameter> parms) {
			mLine = line; mVar = var; mParms = parms;
		}

		public int line() { return mLine; }
		public String name() { return mVar.name(); }
		public Type type() { return mVar.type(); }
		public int nParms() { return mParms.size(); }
		public ArrayList<FunctionParameter> parms() { return mParms; }
	}

	// ******************************* Var class *******************************
	// An object of this class holds the name and type of a variable.
	// Abstract class. Use one of the three subclasses below.

	protected String mName;
	protected Type mType;
	protected boolean mNew = true;

	// Constructors: set name and type per args, but mNew is always true.
	protected Var(String name, boolean isNumeric) { setCommon(name, isNumeric); }
	protected Var(String name, Type type)         { mName = name; mType = type; }
	protected Var(Var from)                       { this(from.mName, from.mType); }

	protected abstract Var copy();						// shallow copy, except mNew is always true

	// Set fields common to all Var objects, except mNew.
	private final void setCommon(String name, boolean isNumeric) {
		mName = name;
		mType = isNumeric ? Type.NUM : Type.STR;
	}

	// Setters
	public void notNew() { mNew = false; }				// mark "not new"
	public void reNew() { mNew = true; }				// mark "new", keep name and type unchanged
	public Var reNew(String name, boolean isNumeric) {	// mark "new", use caller's name and type
		mNew = true;
		setCommon(name, isNumeric);
		return this;
	}

	protected abstract Val newVal();					// attach a new Val to this Var and return it
	protected abstract void val(Val val);				// attach an existing Val to this Var
	protected void arrayDef(ArrayDef def) {				// attach an ArrayDef to this Var (ArrayVar only)
		throw new InvalidParameterException("Var is not ArrayVar");
	}
	protected void fnDef(FnDef def) {					// attach a FnDef to this Var (FunctionVar only)
		throw new InvalidParameterException("Var is not FunctionVar");
	}

	// Getters
	protected String name() { return mName; }
	protected Type type() { return (mType != null) ? mType : Type.NOVAR; }
	protected boolean isNumeric() { return type().isNumeric(); }
	protected boolean isString() { return type().isString(); }
	protected boolean isArray() { return false; }
	protected boolean isFunction() { return false; }
	public boolean isNew() { return mNew; }

	protected abstract Val val();						// return the Val attached to this Var
	protected ArrayDef arrayDef() {
		throw new InvalidParameterException("Var is not ArrayVar");
	}
	protected FnDef fnDef() {
		throw new InvalidParameterException("Var is not FunctionVar");
	}

	// *************************** ScalarVar subclass **************************

	public static class ScalarVar extends Var {
		private Val mVal = null;

		public ScalarVar(String name, boolean isNumeric) { super(name, isNumeric); }
		public ScalarVar(String name, Type type)         { super(name, type); }
		public ScalarVar(Var from)                       { super(from); }

		@Override public Var copy() {						// shallow copy, except mNew is always true
			Var var = new ScalarVar(this);
			var.val(mVal);									// reference same Val
			return var;
		}

		@Override public Val newVal() {						// attach a new scalar Val to this ScalarVar and return it
			mVal = isNumeric() ? new NumVal() : new StrVal();
			return mVal;
		}
		@Override public void val(Val val) { mVal = val; }	// attach an existing Val to this Var

		@Override public Val val() {						// return the Val attached to this ScalarVar
			if (mVal == null) { throw new InvalidParameterException("ScalarVar has no Val"); }
			return mVal;
		}
	} // class ScalarVar

	// *************************** ArrayVar subclass ***************************

	public static class ArrayVar extends Var {
		private ArrayDef mArrayDef = null;					// array metadata and data

		public ArrayVar(String name, boolean isNumeric) { super(name, isNumeric); }
		public ArrayVar(String name, Type type)         { super(name, type); }
		public ArrayVar(Var from)                       { super(from); }

		@Override public Var copy() {						// shallow copy, except mNew is always true
			Var var = new ArrayVar(this);
			var.arrayDef(mArrayDef);						// reference same ArrayDef
			return var;
		}

		@Override public Var reNew(String name, boolean isNumeric) {
			super.reNew(name, isNumeric);					// mark "new", use caller's name and type
			mArrayDef = null;								// destroy array, no need to destroy mVal
			return this;
		}

		@Override public Val newVal() {
			throw new InvalidParameterException("ArrayVar.val(Val): must specify index(es)");
		}
		@Override public void val(Val val) { newVal(); }	// throw exception
		@Override public void arrayDef(ArrayDef def) {		// attach an existing ArrayDef
			mArrayDef = def;
		}

		@Override public boolean isArray() { return true; }

		@Override public Val val() {
			throw new InvalidParameterException("ArrayVar.val(): must specify index(es)");
		}
		// Replaces "Val val()": index an array, put the index and current element value in a new ArrayVal.
		public Val val(ArrayList<Integer> indexList) {
			return mArrayDef.val(indexList);
		}

		@Override public ArrayDef arrayDef() {				// return the ArrayDef attached to this ArrayVar
			if (mArrayDef == null) { throw new InvalidParameterException("ArrayVar has no ArrayDef"); }
			return mArrayDef;
		}
	} // class ArrayVar

	// ************************** FunctionVar subclass *************************

	public static class FunctionVar extends Var {
		private FnDef mFnDef = null;

		public FunctionVar(String name, boolean isNumeric) { super(name, isNumeric); }
		public FunctionVar(String name, Type type)         { super(name, type); }
		public FunctionVar(Var from)                       { super(from); }

		@Override public Var copy() {						// shallow copy, except mNew is always true
			Var var = new FunctionVar(this);
			var.fnDef(mFnDef);								// reference same FnDef
			return var;
		}

		@Override public Val newVal() {
			throw new InvalidParameterException("FunctionVar has no Val");
		}
		@Override public void val(Val val) { newVal(); }		// throw exception
		@Override public void fnDef(FnDef def) { mFnDef = def; }	// attach an existing FnDef

		@Override public boolean isFunction() { return true; }

		@Override public Val val() { return newVal(); }			// throw exception

		@Override public FnDef fnDef() {						// return the FnDef attached to this FunctionVar
			if (mFnDef == null) { throw new InvalidParameterException("FunctionVar has no FnDef"); }
			return mFnDef;
		}
	} // class FunctionVar
}