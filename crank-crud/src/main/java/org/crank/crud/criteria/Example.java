package org.crank.crud.criteria;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Example extends Group {
	private Object example;
	private Set<String> excludedProperties = new HashSet<String>();
	{
		excludedProperties.add("class");
	}
	private boolean generated = false;
	private boolean excludeNulls = true;
	private Operator operator = Operator.EQ;
	private boolean excludeZeroes = false;

	private Example (Object object) {
		this.example = object;
	}
	public Object getExample() {
		return example;
	}

	public void setExample(Object example) {
		this.example = example;
	}
	
	public Example excludeProperty(final String name) {
		excludedProperties.add(name);
		return this;
	}
	public Example excludeNone() {
		excludeNulls = false;
		return this;
	}
	public Example enableLike() {
		operator = Operator.LIKE;
		return this;
	}
	public Example excludeZeroes() {
		excludeZeroes = true;
		return this;
	}
	public Example enableContainsLike() {
		operator = Operator.LIKE_CONTAINS;
		return this;
	}
	public Example enableEndsLike() {
		operator = Operator.LIKE_END;
		return this;
	}
	public Example enableStartsLike() {
		operator = Operator.LIKE_START;
		return this;
	}
	public static Example createExample(Object example) {
		return new Example(example);
	}
	public static Example like(Object object) {
		Example example = new Example(object);
		example.enableContainsLike();
		example.excludeZeroes();
		return example;
	}

	
	@Override
	public Iterator<Criterion> iterator() {
		if (!generated) {
			generated = true;
			generate();
		}
		return super.iterator();
	}
	private void generate() {
		
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(example.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for(PropertyDescriptor pd : propertyDescriptors) {
				String name = pd.getName();
			
				
				if (excludedProperties.contains(name)) {
					continue;
				}
				Object value = pd.getReadMethod().invoke(example, (Object[]) null);
				boolean primitive = pd.getPropertyType().isPrimitive();
				String className = pd.getPropertyType().getName();

				boolean isNull = value == null;
				boolean isString = value instanceof String;
				if (!primitive) {
					if (!isNull && !isString) {
						this.eq(name, value);
					} else if (isString && !isNull) {
						this.add(name, this.operator, value);
					} else {
						if (!this.excludeNulls) {
							if (isString) {
								this.add(name, this.operator, value);
							} else {
								this.eq(name, value);
							}
						}
					}
				} else {
					if (!"boolean".equals(className)) {
						if (excludeZeroes) {
							Number number = (Number) value;
							if (!(number.floatValue()==0.0f)) {
								this.eq(name, value);	
							}
						} else {
							this.eq(name, value);
						}
					} else {
						this.eq(name, value);
					}
				}
			}
		} catch (Exception ex) {
			throw new RuntimeException ("Unable to use example object " + example, ex);
		}
	}
	@Override
	public String toString() {
		if (!generated) {
			generated = true;
			generate();
		}
		return super.toString();
	}
	
	
}
