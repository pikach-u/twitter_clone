const ProfileStats = ({ postCount, followersCount, followingCount }) => {
  const stats = [
    { label: "tweets", count: postCount || 0 },
    { label: "followers", count: followersCount || 0 },
    { label: "following", count: followingCount || 0 },
  ];

  return (
    <div className="flex justify-around mt-6 py-4">
      {stats.map((stat, index) => (
        <button
          key={stat.label}
          className={`text-center transition-opacity ${
            index > 0 ? "hover:opacity-70 cursor-pointer" : ""
          }`}
        >
          <p className="font-semibold">{stat.count}</p>
          <p className="text-gray-500 text-sm">{stat.label}</p>
        </button>
      ))}
    </div>
  );
};

export default ProfileStats;