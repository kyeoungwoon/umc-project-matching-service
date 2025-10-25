'use client';

const UpmsHeader = ({ section }: { section: { title: string; description: string } }) => {
  return (
    <header className="sticky top-0 z-40 w-full border-b bg-white/90 backdrop-blur">
      <div className="container mx-auto flex h-16 items-center justify-between px-6">
        <div>
          <p className="text-16pxr font-medium">{section.title}</p>
          <p className="text-14pxr text-[#6A7282]">{section.description}</p>
        </div>
      </div>
    </header>
  );
};

export default UpmsHeader;
