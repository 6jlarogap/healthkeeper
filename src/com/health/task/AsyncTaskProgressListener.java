package com.health.task;

/**
 * Интерфейс для передачи состояния фоновой задачи.
 *
 */
public interface AsyncTaskProgressListener {
	public void onProgressUpdate(String... messages);
}
