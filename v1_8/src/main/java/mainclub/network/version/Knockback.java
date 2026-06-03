package mainclub.network.version;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Knockback {

    public static void knockback(Player attacker, final Player victim) {
        Vector direction = victim.getLocation().toVector()
                .subtract(attacker.getLocation().toVector());

        direction.setY(0);

        if (direction.lengthSquared() == 0) return;

        direction.normalize();

        // ⚙️ 3. Knockback base
        double horizontal = 0.42;
        double vertical = victim.isOnGround() ? 0.36 : 0.28;
        double friction = 2.0;

        Vector velocity = victim.getVelocity().clone();

        velocity.setX(velocity.getX() / friction);
        velocity.setY(velocity.getY() / friction);
        velocity.setZ(velocity.getZ() / friction);

        velocity.setX(velocity.getX() - direction.getX() * horizontal);
        velocity.setZ(velocity.getZ() - direction.getZ() * horizontal);
        velocity.setY(velocity.getY() + vertical);

        if (velocity.getY() > 0.4) {
            velocity.setY(0.4);
        }

        // ⚙️ 4. Sprint reset (clave PvP)
        if (attacker.isSprinting()) {
            velocity.multiply(1.1);
            attacker.setSprinting(false);
        }

        // ⚙️ 5. Random controlado
        double random = 0.95 + (Math.random() * 0.1);
        velocity.multiply(random);

        // ⚙️ 6. Aplicar + packet
        victim.setVelocity(velocity);

        CraftPlayer cp = (CraftPlayer) victim;
        EntityPlayer ep = cp.getHandle();

        PacketPlayOutEntityVelocity packet = new PacketPlayOutEntityVelocity(
                victim.getEntityId(),
                velocity.getX(),
                velocity.getY(),
                velocity.getZ()
        );

        ep.playerConnection.sendPacket(packet);
    }

    /*@Override
    public void a(Entity entity, float strength, double dx, double dz) {
        Bukkit.broadcastMessage("asd");
            this.ai = true;

            double horizontal = 0.42;
            double vertical = 0.36;
            double friction = 2.0;

            float dist = MathHelper.sqrt(dx * dx + dz * dz);
            if (dist <= 0) return;


            this.motX /= friction;
            this.motY /= friction;
            this.motZ /= friction;


            this.motX -= dx / dist * horizontal;
            this.motZ -= dz / dist * horizontal;
            this.motY += vertical;

            if (this.motY > 0.4D) {
                this.motY = 0.4D;
            }

            //this.move(this.motX, this.motY, this.motZ);
            this.velocityChanged = true;
    }
*/

}