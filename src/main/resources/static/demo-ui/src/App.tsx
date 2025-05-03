import React, { useEffect, useState } from 'react';

type SubscriptionPlan = 'FREE_TRIAL' | 'BASIC' | 'PRO' | 'ENTERPRISE';
interface FeatureFlag {
    key: string;
    enabled: boolean;
}
interface User {
    id: string;
    subscriptionPlan: SubscriptionPlan;
    featureFlags: FeatureFlag[];
}

const USERS = [
    { id: '1', name: 'User A' },
    { id: '2', name: 'User B' }
];

const GET_USER_QUERY = `
  query GetUser($id: ID!) {
    user(id: $id) {
      id
      subscriptionPlan
      featureFlags {
        key
        enabled
      }
    }
  }
`;

const UPDATE_PLAN_MUTATION = `
  mutation UpdateUserPlan($id: ID!, $subscriptionPlan: SubscriptionPlan!) {
    updateUserPlan(id: $id, subscriptionPlan: $subscriptionPlan) {
      id
      subscriptionPlan
      featureFlags {
        key
        enabled
      }
    }
  }
`;

const App: React.FC = () => {
    const [selectedUserId, setSelectedUserId] = useState<string>(USERS[0].id);
    const [plan, setPlan] = useState<SubscriptionPlan>('BASIC');
    const [features, setFeatures] = useState<FeatureFlag[]>([]);
    const [loading, setLoading] = useState<boolean>(false);

    const fetchUser = async (id: string) => {
        setLoading(true);
        const res = await fetch('/graphql', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ query: GET_USER_QUERY, variables: { id } })
        });
        const json = await res.json();
        console.log(json)
        const user: User = json.data.user;
        setPlan(user.subscriptionPlan);
        setFeatures(user.featureFlags);
        setLoading(false);
    };

    const updatePlan = async (id: string, newPlan: SubscriptionPlan) => {
        setLoading(true);
        const res = await fetch('/graphql', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ query: UPDATE_PLAN_MUTATION, variables: { id, subscriptionPlan: newPlan } })
        });
        const json = await res.json();
        const user: User = json.data.updateUserPlan;
        setPlan(user.subscriptionPlan);
        setFeatures(user.featureFlags);
        setLoading(false);
    };

    useEffect(() => { fetchUser(selectedUserId); }, [selectedUserId]);

    return (
        <div style={{ maxWidth: 600, margin: '0 auto', padding: 20 }}>
            <h1>Subscription & Feature Management</h1>

            <label>
                Select User:
                <select
                    value={selectedUserId}
                    onChange={e => setSelectedUserId(e.target.value)}
                    style={{ marginLeft: 10 }}
                >
                    {USERS.map(u => <option key={u.id} value={u.id}>{u.name}</option>)}
                </select>
            </label>

            <div style={{ marginTop: 20 }}>
                <label>
                    Subscription Plan:
                    <select
                        value={plan}
                        onChange={e => setPlan(e.target.value as SubscriptionPlan)}
                        style={{ marginLeft: 10 }}
                    >
                        <option value="FREE_TRIAL">Free Trial</option>
                        <option value="BASIC">Basic</option>
                        <option value="PRO">Pro</option>
                        <option value="ENTERPRISE">Enterprise</option>
                    </select>
                </label>
                <button
                    onClick={() => updatePlan(selectedUserId, plan)}
                    disabled={loading}
                    style={{ marginLeft: 10 }}
                >
                    Save Plan
                </button>
            </div>

            <div style={{ marginTop: 30 }}>
                <h2>Entitled Features</h2>
                {loading ? <p>Loading...</p> : (
                    <ul>
                        {features.map(f => (
                            <li key={f.key}>
                                {f.key} : {f.enabled ? '✅' : '❌'}
                            </li>
                        ))}
                    </ul>
                )}
            </div>
        </div>
    );
};

export default App;
