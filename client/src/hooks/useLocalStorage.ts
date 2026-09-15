import { useCallback, useState } from 'react';

export function useLocalStorage<T>(key: string, initialValue: T) {
  const [stored, setStored] = useState<T>(() => {
    const raw = window.localStorage.getItem(key);
    return raw ? (JSON.parse(raw) as T) : initialValue;
  });

  const setValue = useCallback(
    (value: T) => {
      setStored(value);
      window.localStorage.setItem(key, JSON.stringify(value));
    },
    [key]
  );

  return [stored, setValue] as const;
}
