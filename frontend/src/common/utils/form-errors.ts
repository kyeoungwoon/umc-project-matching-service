/**
 * Helper function to extract all error messages from nested error structure
 * with field path information for better readability
 */
export const extractErrorMessages = (errors: any): string[] => {
  const messages: string[] = [];

  const traverse = (obj: any, path: string = '') => {
    if (!obj) return;

    // If it's a string, it's an error message
    if (typeof obj === 'string') {
      if (path) {
        // Format path for better readability
        const formattedPath = path
          .replace(/\[(\d+)]/g, ' #$1') // Convert [0] to #0
          .replace(/\./g, ' → '); // Convert dots to arrows
        messages.push(`${formattedPath}: ${obj}`);
      } else {
        messages.push(obj);
      }
    } else if (Array.isArray(obj)) {
      // For arrays, traverse each item with index
      obj.forEach((item, index) => {
        traverse(item, path ? `${path}[${index}]` : `[${index}]`);
      });
    } else if (typeof obj === 'object') {
      // For objects, traverse each property
      Object.entries(obj).forEach(([key, value]) => {
        const newPath = path ? `${path}.${key}` : key;
        traverse(value, newPath);
      });
    }
  };

  traverse(errors);
  
  // Filter out empty messages and remove duplicates
  const filtered = messages.filter((msg) => msg && msg.trim().length > 0);
  return Array.from(new Set(filtered));
};

/**
 * Extract error messages without path information (simple format)
 */
export const extractSimpleErrorMessages = (errors: any): string[] => {
  const messages: string[] = [];

  const traverse = (obj: any) => {
    if (!obj) return;

    if (typeof obj === 'string') {
      messages.push(obj);
    } else if (Array.isArray(obj)) {
      obj.forEach((item) => traverse(item));
    } else if (typeof obj === 'object') {
      Object.values(obj).forEach((value) => traverse(value));
    }
  };

  traverse(errors);
  return Array.from(new Set(messages.filter((msg) => msg && msg.trim().length > 0)));
};
