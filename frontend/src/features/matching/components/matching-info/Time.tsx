export const Time = ({ datetime }: { datetime: { date: string; time: string } }) => {
  return (
    <div className="flex items-baseline gap-2 text-xl">
      <p className="font-medium">{datetime.date}</p>
      <p className="text-muted-foreground">{datetime.time}</p>
    </div>
  );
};
