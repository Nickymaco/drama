package org.drama.core;

/**
 * 逻辑处理层描述符
 * @author john
 *
 */
public interface LayerDescriptor {
	enum Default implements LayerDescriptor {
		Drama(Layer.DefaultName, Layer.DefaultUUID, Layer.DefaultPriority);
		
		private String name;
		private String UUID;
		private int priority;

		private Default(String name, String UUID, int priority) {
			this.name = name;
			this.UUID = UUID;
			this.priority = priority;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public int getPriority() {
			return priority;
		}

		@Override
		public String getUUID() {
			return UUID;
		}

		@Override
		public boolean getDisabled() {
			return false;
		}
	};
	
	/**
	 * 名称
	 * @return
	 */
	String getName();
	/**
	 * 优先级
	 * @return
	 */
	int getPriority();
	/**
	 * 唯一标识
	 * @return
	 */
	String getUUID();
	/**
	 * 禁用
	 * @return
	 */
	boolean getDisabled();
}
