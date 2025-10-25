export const formatDate = (dateString?: string) => {
  if (!dateString) return { date: '-', time: '-' };
  const dateObj = new Date(dateString);
  const date = dateObj.toLocaleDateString('ko-KR', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
  });
  const time = dateObj.toLocaleTimeString('ko-KR', {
    hour: '2-digit',
    minute: '2-digit',
  });
  return { date, time };
};
